package com.security.duanspringboot.dto.request.users;

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

    @NotBlank(message = "First name is not blank")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name is not blank")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Email is not blank")
    @Email(message = "Email must be valid")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    @JsonProperty("zip_code")
    private String zipCode;

    @NotBlank(message = "Role is not blank")
    @JsonProperty("role_id")
    private String roleId;
}
