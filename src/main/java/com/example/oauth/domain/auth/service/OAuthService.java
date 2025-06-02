package com.example.oauth.domain.auth.service;

import com.example.oauth.common.config.KakaoOAuthProperties;
import com.example.oauth.common.dto.AuthUser;
import com.example.oauth.common.exception.CustomException;
import com.example.oauth.domain.auth.dto.response.KakaoTokenResponse;
import com.example.oauth.domain.auth.dto.response.KakaoUserInfoResponse;
import com.example.oauth.domain.auth.dto.response.RenewKakaoTokenResponse;
import com.example.oauth.domain.user.entity.User;
import com.example.oauth.domain.user.service.UserCommandService;
import com.example.oauth.domain.user.service.UserQueryService;
import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthService {

    private final KakaoOAuthProperties kakaoOAuthProperties;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final WebClient webClient;

    public KakaoTokenResponse getAccessTokenFromKakao(String code) {
        return webClient.post().uri(kakaoOAuthProperties.getTokenUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoOAuthProperties.getClientId())
                        .with("code", code)
                        .with("redirect_uri", kakaoOAuthProperties.getRedirectUri()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter While Get Access Token")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error While Get Access Token")))
                .bodyToMono(KakaoTokenResponse.class)
                .block();

    }

    public KakaoUserInfoResponse getUserInfoFromKakao(String accessToken) {
        return webClient.get().uri(kakaoOAuthProperties.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter While Get User Info")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error While Get User Info")))
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }

    public User signInWithKakao(HttpSession session, HttpServletResponse response, String code) {
        KakaoTokenResponse kakaoTokenResponse = getAccessTokenFromKakao(code);
        KakaoUserInfoResponse kakaoUserInfoResponse = getUserInfoFromKakao(kakaoTokenResponse.getAccessToken());

        // 이메일 정보가 없다면 회원가입
        User user = userQueryService.getByEmail(kakaoUserInfoResponse.getKakaoAccount().getEmail())
                .orElseGet(() -> userCommandService.createByKakao(
                        kakaoUserInfoResponse.getKakaoAccount().getEmail()
                        ,kakaoUserInfoResponse.getId()
                        ,kakaoTokenResponse.getRefreshToken()
                ));

        AuthUser authUser = AuthUser.create(user.getId());
        session.setAttribute("authUser", authUser);

        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(24 * 60 * 60);
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);
        return user;
    }

    public void renewKakaoAccessToken(Long userId) {
        User foundUser = userQueryService.findById(userId);
        String oauthRefreshToken = foundUser.getOauthRefreshToken();

        RenewKakaoTokenResponse renewKakaoTokenResponse = webClient.post().uri(kakaoOAuthProperties.getTokenUri())
                .body(BodyInserters
                        .fromFormData("grant_type", "refresh_token")
                        .with("client_id", kakaoOAuthProperties.getClientId())
                        .with("refresh_token", oauthRefreshToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(RenewKakaoTokenResponse.class)
                .block();

         if (renewKakaoTokenResponse.getRefreshToken() != null)  {
             foundUser.updateRefreshToken(renewKakaoTokenResponse.getRefreshToken());
         }
    }
}
