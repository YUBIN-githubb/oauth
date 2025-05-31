package com.example.oauth.domain.auth.controller;

import com.example.oauth.common.annotation.Auth;
import com.example.oauth.common.dto.AuthUser;
import com.example.oauth.domain.auth.dto.request.SignInRequest;
import com.example.oauth.domain.auth.dto.request.SignUpRequest;
import com.example.oauth.domain.auth.dto.response.SignUpResponse;
import com.example.oauth.domain.auth.service.AuthService;
import com.example.oauth.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp (
            @Valid @RequestBody SignUpRequest dto
    ) {
        return ResponseEntity.ok(SignUpResponse.of(authService.signUp(dto.getEmail(), dto.getPassword())));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(
            HttpSession session, HttpServletResponse response,
            @Valid @RequestBody SignInRequest dto
    ) {
        authService.signIn(session, response, dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok("Success Sign In");
    }

    @PostMapping("/auth-check")
    public ResponseEntity<String> authCheck(
            @Auth AuthUser authUser
    ) {
        Long userId = authUser.getId();
        return ResponseEntity.ok("User ID: " + userId);
    }
}
