package com.example.oauth.domain.auth.dto.request;

import com.example.oauth.common.consts.Const;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {

    @NotBlank(message = "이메일 입력은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;


}
