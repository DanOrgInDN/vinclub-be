package com.security.duanspringboot.dto.response.orderdetail;

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
public class ProductOrderDetailListResponse {

    @JsonProperty("product_order_id")
    private String productOrderId;

    @JsonProperty("product_category_id")
    private String productCategoryId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("category_name")
    private String  categoryName;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("subtotal")
    private Double subtotal;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("stock")
    private Integer stock;
}
