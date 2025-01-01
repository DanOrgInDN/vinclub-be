package com.security.duanspringboot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.duanspringboot.core.response.ErrorData;
import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.categories.*;
import com.security.duanspringboot.dto.response.categories.CategoryListDetailResponse;
import com.security.duanspringboot.dto.response.categories.CategoryResponse;
import com.security.duanspringboot.entity.CategoryModel;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.repository.CategoryRepository;
import com.security.duanspringboot.repository.ProductCategoryRepository;
import com.security.duanspringboot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.security.duanspringboot.core.response.ResponseStatus.*;
import static com.security.duanspringboot.utils.DateTimeUtils.convertToGMTPlus7;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private static final String DEFAULT_SORT_FIELD = "categoryName";

    @Override
    public ResponseBody<Object> getAllCategories() {
        List<CategoryModel> categories = categoryRepository.findAll();
        if (Objects.isNull(categories)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, CATEGORY_NOT_FOUND, errorMapping);
        }
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (CategoryModel category : categories) {
            var categoryResponse = CategoryResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .minQuantity(category.getMinQuantity())
                    .modifyDate(convertToGMTPlus7(category.getModifyDate()))
                    .build();
            categoryResponses.add(categoryResponse);
        }
        categoryResponses = categoryResponses.stream().sorted(Comparator.comparing(CategoryResponse::getCategoryName)).toList();
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, categoryResponses);
        return response;
    }

    @Override
    public ResponseBody<Object> createCategories(CategoryCreateRequest request) {
        var categoriesModel = categoryRepository.existsByCategoryName(request.getCategoryName());
        if (categoriesModel) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, CATEGORY_NAME_EXIST, errorMapping);
        }

        String categoryId = UUID.randomUUID().toString().replaceAll("-", "");
        var category = CategoryModel.builder()
                .categoryId(categoryId)
                .categoryName(request.getCategoryName())
                .minQuantity(request.getMinQuantity())
                .createDate(LocalDateTime.now())
                .build();
        categoryRepository.save(category);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("categoryId", categoryId);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> updateCategory(CategoryUpdateRequest request) {
        var categoriesModel = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, CATEGORY_NOT_FOUND, errorMapping);
        });
        this.validateCategoryName(request.getCategoryName(), categoriesModel.getCategoryName(), categoriesModel.getCategoryId());

        categoriesModel.setCategoryName(request.getCategoryName());
        categoriesModel.setMinQuantity(request.getMinQuantity());
        categoriesModel.setModifyDate(LocalDateTime.now());
        categoryRepository.save(categoriesModel);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("categoryId", request.getCategoryId());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> deleteCategoryById(String categoryId) {
        var categoriesModel = categoryRepository.findById(categoryId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, CATEGORY_NOT_FOUND, errorMapping);
        });
        productCategoryRepository.deleteAllByCategoryId(categoryId);
        categoryRepository.deleteById(categoriesModel.getCategoryId());

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("categoryId", categoryId);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Transactional
    @Override
    public ResponseBody<Object> deleteProductCategoryByCategoryId(String categoryId) {
        var categoriesModel = categoryRepository.findById(categoryId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, CATEGORY_NOT_FOUND, errorMapping);
        });
        productCategoryRepository.deleteAllByCategoryId(categoryId);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("categoryId", categoryId);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    private CategoryListDetailResponse buildCategoryModelResponse(CategoryModel categoryModel) {
        CategoryListDetailResponse categoryListResponse = CategoryListDetailResponse.builder()
                .categoryId(categoryModel.getCategoryId())
                .categoryName(categoryModel.getCategoryName())
                .createDate(categoryModel.getCreateDate())
                .modifyDate(categoryModel.getModifyDate())
                .build();
        return categoryListResponse;
    }

    private void validateCategoryName(String categoryName, String categoryNamePresent, String categoryId) {
        var categoryModel = categoryRepository.findByCategoryName(categoryName);
        if (!categoryModel.getCategoryId().equalsIgnoreCase(categoryId) && !Objects.equals(categoryName, categoryNamePresent) && !Objects.equals(categoryModel.getCategoryName(), categoryNamePresent)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, CATEGORY_NAME_EXIST, errorMapping);
        }
    }
}
