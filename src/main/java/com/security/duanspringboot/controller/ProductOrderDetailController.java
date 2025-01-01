package com.security.duanspringboot.controller;

import com.security.duanspringboot.dto.request.orderdetail.ProductOrderDetailCreateRequest;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.service.ProductOrderDetailService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductOrderDetailController {

    private final ProductOrderDetailService productOrderDetailService;
    private final Validator validator;

    @PostMapping("/un_auth/product_order_detail/create")
    public ResponseEntity<Object> createProductOrderDetail(@RequestBody ProductOrderDetailCreateRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productOrderDetailService.createProductOrderDetail(request));
    }

    @GetMapping("/un_auth/product_order_detail/{product_order_id}")
    public ResponseEntity<Object> getProductOrderDetail(@PathVariable("product_order_id") String productOrderId) {
        return ResponseEntity.ok(productOrderDetailService.getProductOrderDetail(productOrderId));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
