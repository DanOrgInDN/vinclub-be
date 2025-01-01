package com.security.vinclub.dto.response.categories;

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
public class CategoryListDetailStatusResponse {

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_parent_id")
    private List<CategoryListDetailStatusResponse> categoryParentId;

    @JsonProperty("category_type")
    private String categoryType;

    @JsonProperty("display_status")
    private String displayStatus;

    @JsonProperty("display_position")
    private Integer displayPosition;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("modify_date")
    private LocalDateTime modifyDate;
}
