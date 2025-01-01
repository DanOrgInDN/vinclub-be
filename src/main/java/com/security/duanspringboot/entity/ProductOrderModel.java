package com.security.duanspringboot.entity;

import com.security.duanspringboot.repository.ProductOrderRepository;
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
@Entity(name = ProductOrderRepository.TABLE)
public class ProductOrderModel {

    @Id
    @Column(nullable = false)
    private String productOrderId;
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Double totalAmount;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private String note;
    private String status;
    private String trackingNumber;
    private Boolean isDelete;
    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOrderDetailModel> orderDetails;
}
