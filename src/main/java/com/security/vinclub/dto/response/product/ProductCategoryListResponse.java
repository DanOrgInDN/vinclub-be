package com.security.vinclub.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryListResponse {

    @JsonProperty("product_category_id")
    private String productCategoryId;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_parent_id")
    private List<ProductCategoryListResponse> categoryParentId;

    @JsonProperty("category_type")
    private String categoryType;
}
