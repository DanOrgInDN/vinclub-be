package com.security.vinclub.dto.request.authen;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpUserRequest {

    @NotBlank(message = "Name is not blank")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Password is not blank")
    private String password;

    @NotBlank(message = "Email is not blank")
    private String email;

    @NotBlank(message = "Role is not blank")
    @JsonProperty("role_id")
    private String roleId;
}
