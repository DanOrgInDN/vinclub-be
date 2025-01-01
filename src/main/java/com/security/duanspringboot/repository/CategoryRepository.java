package com.security.duanspringboot.repository;

import com.security.duanspringboot.entity.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, String> {
    String TABLE = "category";

    boolean existsByCategoryName(String categoryName);
    CategoryModel findByCategoryName(String categoryName);

    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE categoryId IN (:categoryId)", nativeQuery = true)
    List<CategoryModel> findAllByCategoryId(@Param("categoryId") List<String> categoryId);

    @Query("SELECT c FROM category c WHERE c.categoryId IN :ids")
    List<CategoryModel> findAllByCategoryIdIn(List<String> ids);
}
