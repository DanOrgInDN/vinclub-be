package com.security.duanspringboot.dto.request.categories;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryCreateRequest {

    @NotBlank(message = "Category name not blank")
    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("min_quantity")
    private String minQuantity;
}
