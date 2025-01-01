package com.security.duanspringboot.controller;

import com.security.duanspringboot.dto.request.ProductCategoryRequest;
import com.security.duanspringboot.dto.request.categories.CategoryUpdateRequest;
import com.security.duanspringboot.dto.request.productcategory.ProductCategoryImportRequest;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.service.ProductCategoryService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;
    private final Validator validator;
    @GetMapping("/un_auth/product_category/all")
    public ResponseEntity<Object> getAllProductCategory() {
        return ResponseEntity.ok(productCategoryService.getAllProductCategory());
    }

    @GetMapping("/un_auth/product_category/{category_id}/{type}")
    public ResponseEntity<Object> getAllProductCategoryByCategoryId(@PathVariable("category_id") String categoryId, @PathVariable("type") String type) {
        return ResponseEntity.ok(productCategoryService.getAllProductCategoryByCategoryId(categoryId, type));
    }

    @PostMapping("/un_auth/product_category/create")
    public ResponseEntity<Object> createProductCategory(@RequestBody ProductCategoryRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productCategoryService.createProductCategory(request));
    }

    @PostMapping("/un_auth/product_category/update")
    public ResponseEntity<Object> updateProductCategory(@RequestBody ProductCategoryRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productCategoryService.updateProductCategory(request));
    }

    @DeleteMapping("/un_auth/product_category/delete/{category_id}")
    public ResponseEntity<Object> deleteProductCategory(@PathVariable("category_id") String categoryId) {
        return ResponseEntity.ok(productCategoryService.deleteProductCategoryById(categoryId));
    }

    @PostMapping("/un_auth/product_category/import")
    public ResponseEntity<Object> importProducts(@RequestBody ProductCategoryImportRequest request) {
        return ResponseEntity.ok(productCategoryService.importExcel(request));
    }

    @PostMapping("/un_auth/product_category/import/verify/{category_id}")
    public ResponseEntity<Object> verifyImportProducts(@PathVariable("category_id") String categoryId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(productCategoryService.verifyImportProducts(categoryId, file));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
