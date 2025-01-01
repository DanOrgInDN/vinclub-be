package com.security.duanspringboot.entity;

import com.security.duanspringboot.repository.CategoryRepository;
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
@Entity(name = CategoryRepository.TABLE)
public class CategoryModel {

    @Id
    @Column(nullable = false)
    private String categoryId;
    @Column(unique = true)
    private String categoryName;
    private String minQuantity;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryModel> productCategories;
}
