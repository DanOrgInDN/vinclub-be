package com.security.vinclub.controller;

import com.security.vinclub.dto.request.deposit.CreateDepositRequest;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.service.DepositService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deposit")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;
    private final Validator validator;

    @PostMapping("/create")
    public ResponseEntity<Object> createDepositTransaction(@RequestBody CreateDepositRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(depositService.createDepositTransaction(request));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
