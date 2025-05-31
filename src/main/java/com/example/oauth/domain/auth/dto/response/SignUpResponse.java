package com.example.oauth.domain.auth.dto.response;

import com.example.oauth.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SignUpResponse {
    private final Long id;
    private final String email;

    private SignUpResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static SignUpResponse of(User user) {
        return new SignUpResponse(user.getId(), user.getEmail());
    }
}
