package com.security.duanspringboot.controller;

import com.security.duanspringboot.dto.request.order.ProductOrderCreateRequest;
import com.security.duanspringboot.dto.request.order.ProductOrderSearchRequest;
import com.security.duanspringboot.dto.request.order.ProductOrderUpdateRequest;
import com.security.duanspringboot.dto.request.order.StartShippingRequest;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.service.ProductOrderService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductOrderController {

    private final ProductOrderService productOrderService;
    private final Validator validator;

    @GetMapping("/un_auth/product_order/product_order_list")
    public ResponseEntity<Object> getAllProductOrder() {
        return ResponseEntity.ok(productOrderService.getAllProductOrder());
    }

    @PostMapping("/un_auth/product_order/create")
    public ResponseEntity<Object> createProductOrder(@RequestBody ProductOrderCreateRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productOrderService.createProductOrder(request));
    }

    @GetMapping("/un_auth/product_order/{product_order_id}")
    public ResponseEntity<Object> getProductOrderDetail(@PathVariable("product_order_id") String productOrderId) {
        return ResponseEntity.ok(productOrderService.getProductOrderByIdDetail(productOrderId));
    }

    @GetMapping("/un_auth/product_order_status/{product_order_id}")
    public ResponseEntity<Object> getProductOrderDetailStatus(@PathVariable("product_order_id") String productOrderId) {
        return ResponseEntity.ok(productOrderService.getProductOrderStatusByIdDetail(productOrderId));
    }

    @GetMapping("/un_auth/product_order/tracking_number/{tracking_number}")
    public ResponseEntity<Object> checkTrackingNumber(@PathVariable("tracking_number") String trackingNumber) {
        return ResponseEntity.ok(productOrderService.checkTrackingNumber(trackingNumber));
    }

    @PostMapping("/un_auth/product_order/product_order_update")
    public ResponseEntity<Object> updateProductOrder(@RequestBody ProductOrderUpdateRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productOrderService.updateProductOrderStatus(request));
    }

    @DeleteMapping("/un_auth/product_order/delete/{product_order_id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("product_order_id") String productOrderId) {
        return ResponseEntity.ok(productOrderService.deleteProductOrderById(productOrderId));
    }

    @PostMapping("/un_auth/product_order/get_all_order_product")
    public ResponseEntity<Object> getAllProduct(@RequestBody ProductOrderSearchRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productOrderService.getAllProductOrderPage(request));
    }

    @PostMapping("/un_auth/product_order/start_shipping")
    public ResponseEntity<Object> startShipping(@RequestBody StartShippingRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productOrderService.startShipping(request));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
