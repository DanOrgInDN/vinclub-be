package com.security.vinclub.dto.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.vinclub.dto.response.orderdetail.ProductOrderDetailListResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponse {

    @JsonProperty("product_order_id")
    private String productOrderId;

    @JsonProperty("userId")
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

    @JsonProperty("is_delete")
    private Boolean isDelete;

    @JsonProperty("product_order_detail_list_responses")
    private List<ProductOrderDetailListResponse> productOrderDetailListResponses;
}
