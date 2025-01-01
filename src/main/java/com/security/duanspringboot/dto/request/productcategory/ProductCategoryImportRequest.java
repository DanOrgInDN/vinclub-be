package com.security.duanspringboot.dto.request.productcategory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.duanspringboot.dto.response.productcategory.ProductCategoryImportDataResponse;
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
public class ProductCategoryImportRequest {
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("import_data")
    List<ProductCategoryImportDataResponse> importData;
}
