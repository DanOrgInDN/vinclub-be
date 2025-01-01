package com.security.duanspringboot.dto.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderListResponse {

    @JsonProperty("product_order_id")
    private String productOrderId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("total_amount")
    private Double totalAmount;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("note")
    private String note;

    @JsonProperty("status")
    private String status;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("is_delete")
    private Boolean isDelete;
}
