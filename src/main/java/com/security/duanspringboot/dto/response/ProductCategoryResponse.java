package com.security.duanspringboot.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductCategoryResponse {
    @JsonProperty("product_category_id")
    private String productCategoryId;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("min_limit")
    private Integer minLimit;
    @JsonProperty("max_limit")
    private Integer maxLimit;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("create_date")
    private LocalDateTime createDate;
    @JsonProperty("modify_date")
    private LocalDateTime modifyDate;
    @JsonProperty("keywords")
    List<String> keywords;
    @JsonProperty("min_quantity")
    private String minQuantity;
    @JsonProperty("generic_name")
    List<String> genericName;

    public ProductCategoryResponse(String productCategoryId, String productId, String productName, String categoryName, String categoryId, Integer quantity, Integer minLimit, Integer maxLimit, Double price, LocalDateTime createDate, LocalDateTime modifyDate, String minQuantity) {
        this.productCategoryId = productCategoryId;
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.price = price;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.minQuantity = minQuantity;
    }
}
