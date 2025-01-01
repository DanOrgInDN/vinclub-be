package com.security.duanspringboot.service;

import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.categories.*;

public interface CategoryService {

    ResponseBody<Object> getAllCategories();

    ResponseBody<Object> createCategories(CategoryCreateRequest request);

    ResponseBody<Object> updateCategory(CategoryUpdateRequest request);

    ResponseBody<Object> deleteCategoryById(String categoryId);

    ResponseBody<Object> deleteProductCategoryByCategoryId(String categoryId);
}
