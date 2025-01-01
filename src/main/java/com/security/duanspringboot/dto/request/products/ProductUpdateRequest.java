package com.security.duanspringboot.dto.request.products;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductUpdateRequest {

    @NotBlank(message = "Product id is not blank")
    @JsonProperty("product_id")
    private String productId;

    @NotBlank(message = "Product name is not blank")
    @JsonProperty("product_name")
    private String productName;

    @Min(value = 0, message = "Quantity must be >= 0")
    private Integer quantity;

    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("generic_name")
    private List<String> genericName;

    @Min(value = 0, message = "Min limit must be >= 0")
    private Integer minLimit;

    @Min(value = 0, message = "Max limit must be >= 0")
    private Integer maxLimit;
}
