package com.security.duanspringboot.repository;

import com.security.duanspringboot.dto.response.ProductCategoryResponse;
import com.security.duanspringboot.entity.ProductCategoryModel;
import com.security.duanspringboot.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryModel, String> {
    String TABLE = "product_category";

    @Query("SELECT new com.security.duanspringboot.dto.response.ProductCategoryResponse(pc.productCategoryId, pc.productId, p.productName, c.categoryName, pc.categoryId, pc.quantity, pc.minLimit, pc.maxLimit, pc.price, pc.createDate, pc.modifyDate, c.minQuantity) " +
            "FROM product_category pc JOIN product p ON pc.productId = p.productId JOIN category c ON pc.categoryId = c.categoryId ")
    List<ProductCategoryResponse> findAllIncludeProductName();

    @Query("SELECT new com.security.duanspringboot.dto.response.ProductCategoryResponse(pc.productCategoryId, pc.productId, p.productName, c.categoryName, pc.categoryId, pc.quantity, pc.minLimit, pc.maxLimit, pc.price, pc.createDate, pc.modifyDate, c.minQuantity) " +
            "FROM product_category pc JOIN product p ON pc.productId = p.productId JOIN category c ON pc.categoryId = c.categoryId " +
            "WHERE pc.categoryId = :categoryId")
    List<ProductCategoryResponse> findAllIncludeProductNameByCategoryId(String categoryId);

    void deleteAllByCategoryId(String categoryId);

    void deleteAllByProductId(String productId);
    boolean existsByProductIdAndCategoryId(String productId, String categoryId);

    @Query("SELECT pc FROM product_category pc WHERE pc.productId in :productIds AND pc.categoryId = :categoryId")
    List<ProductCategoryModel> findByProductIdIn(List<String> productIds, String categoryId);

    @Query("SELECT pc FROM product_category pc WHERE pc.productId in :productIds AND pc.categoryId IN :categoryIds")
    List<ProductCategoryModel> findByProductIdInAndCaterogyIdIn(List<String> productIds, List<String> categoryIds);

    @Query("SELECT pc FROM product_category pc WHERE pc.productCategoryId IN :ids")
    List<ProductCategoryModel> findAllByProductCategoryIdIn(List<String> ids);

    @Query("SELECT pc FROM product_category pc WHERE pc.categoryId = :categoryId")
    List<ProductCategoryModel> findByCategoryId(String categoryId);

}
