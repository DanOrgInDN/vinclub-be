package com.security.duanspringboot.entity;

import com.security.duanspringboot.repository.ProductOrderDetailRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = ProductOrderDetailRepository.TABLE)
public class ProductOrderDetailModel {

    @Id
    @Column(nullable = false)
    private String productOrderDetailId;
    private String productOrderId;
    private String productCategoryId;
    private String categoryId;
    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double subtotal;
    private LocalDateTime createDate;
    @ManyToOne
    @JoinColumn(name = "productOrderId", nullable = false, insertable = false, updatable = false)
    private ProductOrderModel productOrder;

    @ManyToOne()
    @JoinColumn(name = "productCategoryId", nullable = false, insertable = false, updatable = false)
    private ProductCategoryModel productCategory;

}
