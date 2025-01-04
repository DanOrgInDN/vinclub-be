package com.security.vinclub.entity;

import com.security.vinclub.repository.FileStorageRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = FileStorageRepository.TABLE)
public class FileStorage {
    @Id
    @Column(nullable = false)
    private String fileId;
    private String fileDirectory;
    private String rawFileName;
    private String fileName;
    private String fileExtension;
    private String description;
    private String fileType;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
