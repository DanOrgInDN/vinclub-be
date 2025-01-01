package com.security.duanspringboot.dto.request.authen;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Username is not blank")
    private String email;

    @NotBlank(message = "Old Password is not blank")
    private String oldPassword;

    @NotBlank(message = "New Password is not blank")
    private String newPassword;
}
