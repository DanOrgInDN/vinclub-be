package com.security.duanspringboot.dto.request.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.duanspringboot.enumeration.TypePaymentMethod;
import com.security.duanspringboot.utils.EnumValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOrderCreateRequest {

    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message = "Full name is not blank")
    @JsonProperty("full_name")
    private String fullName;

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone number is not blank")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Min(value = 0, message = "Total amount must be >= 0")
    @JsonProperty("total_amount")
    private Double totalAmount;

    @JsonProperty("shipping_address")
    @NotBlank(message = "Address is not blank")
    private String shippingAddress;

    private String note;

    @JsonProperty("code")
    private String code;
}
