package com.security.vinclub.dto.request.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.vinclub.utils.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileStorageUploadRequest {
    @NotNull(message = "File is not null")
    private MultipartFile file;

    private String file_name;
    private String description;
    private String file_directory;
    private String doc_type_id;

}