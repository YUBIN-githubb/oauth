package com.example.oauth.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthUser {
    private String id;
    private String email;

    private OAuthUser(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public static OAuthUser create(String id, String email) {
        return new OAuthUser(id, email);
    }
}
