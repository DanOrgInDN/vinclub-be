package com.security.duanspringboot.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeListResponse {

    @JsonProperty("product_attribute_id")
    private String productAttributeId;

    @JsonProperty("attribute_id")
    private String attributeId;

    @JsonProperty("attribute_name")
    private String attributeName;

    @JsonProperty("attribute_parent_id")
    private String attributeParentId;

    @JsonProperty("attribute_type")
    private String attributeType;
}
