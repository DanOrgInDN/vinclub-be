package com.security.duanspringboot.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeStatusOrder {
    PENDING("Đang chờ"),
    CONFIRMED("Đã xác nhận"),
    SHIPPING("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã hủy"),
    RETURNED("Đã trả hàng"),
    COMPLETED("Đã hoàn thành");

    private final String value;

}
