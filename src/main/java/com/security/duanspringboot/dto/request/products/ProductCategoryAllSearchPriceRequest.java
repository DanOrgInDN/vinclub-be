package com.security.duanspringboot.dto.request.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryAllSearchPriceRequest {

    @NotBlank(message = "Category id is not blank")
    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("price_to")
    private Double priceTo;

    @JsonProperty("price_from")
    private Double priceFrom;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_code")
    private String productCode;

    @NotBlank(message = "Page number is not blank")
    @JsonProperty("page_number")
    private String pageNumber;

    @NotBlank(message = "Page size is not blank")
    @JsonProperty("page_size")
    private String pageSize;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_direction")
    private String sortDirection;
}
