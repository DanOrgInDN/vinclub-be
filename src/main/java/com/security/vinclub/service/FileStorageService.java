package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    ResponseBody<Object> uploadAvatar(MultipartFile file, String userId);
}
