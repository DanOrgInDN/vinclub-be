package com.security.duanspringboot.repository;

import com.security.duanspringboot.entity.ProductOrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderModel, String> {
    String TABLE = "products_order";

    @Query("SELECT po FROM products_order po WHERE po.orderDate BETWEEN :startDate AND :endDate")
    List<ProductOrderModel> findByOrderDateBetween(String startDate, String endDate);

    @Query("SELECT po FROM products_order po WHERE po.orderDate BETWEEN :startDate AND :endDate AND po.status = :status")
    List<ProductOrderModel> findByOrderDateBetweenAndStatus(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("status") String status);


    ProductOrderModel findByProductOrderIdAndIsDelete(String productOrderId, Boolean isDelete);
    ProductOrderModel findByTrackingNumberAndIsDelete(String trackingNumber, Boolean isDelete);
    List<ProductOrderModel> findAllByIsDelete(Boolean isDelete);
    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE productOrderId LIKE %:productOrderId% AND status LIKE %:status% AND isDelete =:isDelete ", nativeQuery = true)
    Page<ProductOrderModel> findByProductOrderIdAndStatusAndIsDelete(@Param("productOrderId") String productOrderId, @Param("status") String status, @Param("isDelete") Boolean isDelete, Pageable pageable);
}
