package com.security.vinclub.controller;

import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.service.FileStorageService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;
    private final Validator validator;

    @PostMapping(value = "/avatar/upload/{user_id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable("user_id") String userId) {
        return ResponseEntity.ok(fileStorageService.uploadAvatar(file, userId));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}