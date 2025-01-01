package com.security.duanspringboot.controller;

import com.security.duanspringboot.dto.request.categories.*;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.service.CategoryService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final Validator validator;

    @GetMapping("/un_auth/category/all")
    public ResponseEntity<Object> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @PostMapping("/un_auth/category/category_create")
    public ResponseEntity<Object> createCategory(@RequestBody CategoryCreateRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(categoryService.createCategories(request));
    }

    @PostMapping("/un_auth/category/category_update")
    public ResponseEntity<Object> updateCategoriesDetail(@RequestBody CategoryUpdateRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(categoryService.updateCategory(request));
    }

    @DeleteMapping("/un_auth/category/delete/{category_id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable("category_id") String categoryId) {
        return ResponseEntity.ok(categoryService.deleteCategoryById(categoryId));
    }

    @DeleteMapping("/un_auth/product_category/all/delete/{category_id}")
    public ResponseEntity<Object> deleteProductCategoryByCategoryId(@PathVariable("category_id") String categoryId) {
        return ResponseEntity.ok(categoryService.deleteProductCategoryByCategoryId(categoryId));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
