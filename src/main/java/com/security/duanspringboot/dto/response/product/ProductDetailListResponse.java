package com.security.duanspringboot.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.duanspringboot.dto.response.categories.CategoryListDetailResponse;
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
public class ProductDetailListResponse {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("modify_date")
    private LocalDateTime modifyDate;

    @JsonProperty("keywords")
    private List<String> keywords;
}
