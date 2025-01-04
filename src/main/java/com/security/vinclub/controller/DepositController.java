package com.security.vinclub.controller;

import com.security.vinclub.dto.request.deposit.CreateDepositRequest;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.service.DepositService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Object> approveDeposit(@PathVariable("id") String id) {
        return ResponseEntity.ok(depositService.approveDeposit(id));
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<Object> rejectDeposit(@PathVariable("id") String id) {
        return ResponseEntity.ok(depositService.rejectDeposit(id));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
