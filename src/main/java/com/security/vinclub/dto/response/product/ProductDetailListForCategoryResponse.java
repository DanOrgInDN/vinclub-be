package com.security.vinclub.dto.response.product;

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
public class ProductDetailListForCategoryResponse {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("description_short")
    private String descriptionShort;

    @JsonProperty("description_long")
    private String descriptionLong;

    private Double price;

    @JsonProperty("promotion_price")
    private Double promotionPrice;

    @JsonProperty("percent_discount")
    private Double percentDiscount;

    private int quantity;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("modify_date")
    private LocalDateTime modifyDate;
}
