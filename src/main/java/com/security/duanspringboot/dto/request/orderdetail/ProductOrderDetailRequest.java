package com.security.duanspringboot.dto.request.orderdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductOrderDetailRequest {

    @NotBlank(message = "Product Order Id is not blank")
    @JsonProperty("product_order_id")
    private String productOrderId;

    @NotBlank(message = "Product Category Id is not blank")
    @JsonProperty("product_category_id")
    private String productCategoryId;

    @NotBlank(message = "Product Name is not blank")
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("quantity")
    private Integer quantity;

    @Min(value = 0, message = "Price must be >= 0")
    @JsonProperty("price")
    private Double price;

    @Min(value = 0, message = "Subtotal must be >= 0")
    @JsonProperty("subtotal")
    private Double subtotal;
}