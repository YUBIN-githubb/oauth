package com.example.oauth.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUser {
    private Long id;

    @JsonCreator
    private AuthUser(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public static AuthUser create(Long id) {
        return new AuthUser(id);
    }
}
