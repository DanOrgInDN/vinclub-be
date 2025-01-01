package com.security.duanspringboot.dto.request.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.duanspringboot.dto.request.orderdetail.ProductOrderDetailRequest;
import com.security.duanspringboot.enumeration.TypeStatusOrder;
import com.security.duanspringboot.utils.EnumValue;
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
public class ProductOrderUpdateRequest {

    @NotBlank(message = "Product order id not blank")
    @JsonProperty("product_order_id")
    private String productOrderId;

    @NotBlank(message = "Status is not blank")
    @EnumValue(name = "status", enumClass = TypeStatusOrder.class)
    @JsonProperty("status")
    private String status;

    @JsonProperty("product_order_details")
    private List<ProductOrderDetailRequest> productOrderDetails;
}
