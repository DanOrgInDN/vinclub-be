package com.security.duanspringboot.dto.response.categories;

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
public class CategoryResponse {
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("min_quantity")
    private String minQuantity;
    private LocalDateTime createDate;
    @JsonProperty("modify_date")
    private LocalDateTime modifyDate;

}

