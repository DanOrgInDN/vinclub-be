package com.security.duanspringboot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCategoryRequest {
    @JsonProperty("product_category_id")
    private String productCategoryId;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("min_limit")
    private Integer minLimit;
    @JsonProperty("max_limit")
    private Integer maxLimit;
    @JsonProperty("create_date")
    private LocalDateTime createDate;
    @JsonProperty("modify_date")
    private LocalDateTime modifyDate;
}