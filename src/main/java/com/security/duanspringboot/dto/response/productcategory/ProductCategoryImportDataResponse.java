package com.security.duanspringboot.dto.response.productcategory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryImportDataResponse {
    @JsonProperty("import_name")
    private String importName;
    @JsonProperty("system_name")
    private String systemName;
    @JsonProperty("quantity")
    private Integer quantity;
}
