package com.example.loginlivesession2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LoginReqDto {

    @NotBlank
    private String userId;
    @NotBlank
    private String password;

    @NotBlank
    private String passwordCheck;

    public LoginReqDto(String userId, String password, String passwordCheck) {
        this.userId = userId;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }

}

