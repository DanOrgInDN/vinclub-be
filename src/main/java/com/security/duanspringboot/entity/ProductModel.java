package com.security.duanspringboot.entity;

import com.security.duanspringboot.repository.ProductRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = ProductRepository.TABLE)
public class ProductModel {

    @Id
    @Column(nullable = false)
    private String productId;
    @Column(unique = true)
    private String productName;
    private String genericName;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    @Column(columnDefinition = "TEXT")
    private String keyword;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryModel> productCategories;
}
