package com.security.vinclub.dto.request.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    @NotBlank(message = "User id is not blank")
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message = "Name is not blank")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Email is not blank")
    @Email(message = "Email must be valid")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Role is not blank")
    @JsonProperty("role_id")
    private String roleId;
}
