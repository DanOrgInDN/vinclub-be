package com.security.vinclub.controller;

import com.security.vinclub.dto.request.file.FileStorageUploadRequest;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.service.FileStorageService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;
    private final Validator validator;

    @PostMapping(value = "/un_auth/files/upload_file",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> uploadFileStorage(FileStorageUploadRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(fileStorageService.uploadFile(request));
    }

    @GetMapping("/un_auth/files/get_info_file_storage/{file_id}")
    public ResponseEntity<Object> getInfoFileStorage(@PathVariable("file_id") String fileId) {
        return ResponseEntity.ok(fileStorageService.getInfoFileStorage(fileId));
    }

    @DeleteMapping("/un_auth/files/delete/{file_id}")
    public ResponseEntity<Object> deleteFile(@PathVariable("file_id") String fileId) throws IOException {
        return ResponseEntity.ok(fileStorageService.deleteFile(fileId));
    }

    @GetMapping(value = "/un_auth/files/download/thumbnail/{fileId}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> downloadFileWithUrl(@PathVariable String fileId) throws IOException {
        return ResponseEntity.ok(fileStorageService.downloadThumbnailWithUrl(fileId));
    }

    @GetMapping(value = "/un_auth/files/download/original/{fileId}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> downloadFileOriginalWithUrl(@PathVariable String fileId) throws IOException {
        return ResponseEntity.ok(fileStorageService.downloadOriginalWithUrl(fileId));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
