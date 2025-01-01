package com.security.duanspringboot.service;

import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.orderdetail.ProductOrderDetailCreateRequest;

public interface ProductOrderDetailService {

    ResponseBody<Object> getProductOrderDetail(String productOrderId);

    ResponseBody<Object> createProductOrderDetail(ProductOrderDetailCreateRequest request);
}
