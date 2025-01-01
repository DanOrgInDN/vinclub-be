package com.security.duanspringboot.service;

import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.ProductCategoryRequest;
import com.security.duanspringboot.dto.request.categories.CategoryUpdateRequest;
import com.security.duanspringboot.dto.request.productcategory.ProductCategoryImportRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductCategoryService {

    ResponseBody<Object> getAllProductCategory();

    ResponseBody<Object> createProductCategory(ProductCategoryRequest request);

    ResponseBody<Object> updateProductCategory(ProductCategoryRequest request);

    ResponseBody<Object> deleteProductCategoryById(String id);

    ResponseBody<Object> getAllProductCategoryByCategoryId(String categoryId, String type);

    ResponseBody<Object> importExcel(ProductCategoryImportRequest request);

    ResponseBody<Object> verifyImportProducts(String categoryId, MultipartFile file);

}
