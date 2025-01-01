package com.security.duanspringboot.service;

import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.products.*;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ResponseBody<Object> getAllProductDetail();

    ResponseBody<Object> createProduct(ProductCreateRequest request);

    ResponseBody<Object> updateProduct(ProductUpdateRequest request);

    ResponseBody<Object> deleteProductById(String productId);

    ResponseBody<Object> importExcel(MultipartFile file);
}
