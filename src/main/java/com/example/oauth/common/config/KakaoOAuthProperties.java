package com.example.oauth.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "kakao")
@AllArgsConstructor
public class KakaoOAuthProperties {
    private String clientId;
    private String tokenUri;
    private String redirectUri;
    private String userInfoUri;
}
