package com.example.oauth.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final String status;
    private final int code;
    private final String message;
    private final LocalDateTime timestamp;

    @Builder
    private ErrorResponse(String status, int code, String message, LocalDateTime timestamp) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }
}
