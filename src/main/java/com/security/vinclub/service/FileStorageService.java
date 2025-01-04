package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.file.FileStorageUploadRequest;

import java.io.IOException;

public interface FileStorageService {
    ResponseBody<Object> uploadFile(FileStorageUploadRequest request);

    byte[] downloadThumbnailWithUrl(String fileId) throws IOException;

    byte[] downloadOriginalWithUrl(String fileId) throws IOException;

    ResponseBody<Object> deleteFile(String fileId) throws IOException;

    ResponseBody<Object> getInfoFileStorage(String fileId);

}
