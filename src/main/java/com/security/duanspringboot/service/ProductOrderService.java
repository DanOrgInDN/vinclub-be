package com.security.duanspringboot.service;

import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.order.ProductOrderCreateRequest;
import com.security.duanspringboot.dto.request.order.ProductOrderSearchRequest;
import com.security.duanspringboot.dto.request.order.ProductOrderUpdateRequest;
import com.security.duanspringboot.dto.request.order.StartShippingRequest;

public interface ProductOrderService {

    ResponseBody<Object> getAllProductOrder();

    ResponseBody<Object> createProductOrder(ProductOrderCreateRequest request);

    ResponseBody<Object> getProductOrderByIdDetail(String productOrderId);

    ResponseBody<Object> checkTrackingNumber(String trackingNumber);

    ResponseBody<Object> updateProductOrderStatus(ProductOrderUpdateRequest request);

    ResponseBody<Object> deleteProductOrderById(String productOrderId);

    ResponseBody<Object> getAllProductOrderPage(ProductOrderSearchRequest request);

    ResponseBody<Object> getProductOrderStatusByIdDetail(String productOrderId);

    ResponseBody<Object> startShipping(StartShippingRequest request);
}
