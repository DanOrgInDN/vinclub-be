package com.security.duanspringboot.dto.request.authen;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank(message = "Username is not blank")
    private String email;

    @NotBlank(message = "Password is not blank")
    private String password;
}
