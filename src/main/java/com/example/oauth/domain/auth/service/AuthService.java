package com.example.oauth.domain.auth.service;

import com.example.oauth.common.config.PasswordEncoder;
import com.example.oauth.common.dto.AuthUser;
import com.example.oauth.common.exception.CustomException;
import com.example.oauth.domain.user.entity.User;
import com.example.oauth.domain.user.repository.UserRepository;
import com.example.oauth.domain.user.service.UserCommandService;
import com.example.oauth.domain.user.service.UserQueryService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    public User signUp(String email, String password) {
        if(userQueryService.existByEmail(email)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Already Existing Email");
        }
        return userCommandService.create(email, passwordEncoder.encode(password));
    }

    public void signIn(HttpSession session, HttpServletResponse response, String email, String password) {
        User foundUser = userQueryService.findByEmail(email);

        if(!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Incorrect Password");
        }

        AuthUser authUser = AuthUser.create(foundUser.getId());
        session.setAttribute("authUser", authUser);

        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(24 * 60 * 60);
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);
    }
}
