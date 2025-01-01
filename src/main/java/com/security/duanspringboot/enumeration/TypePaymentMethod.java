package com.security.duanspringboot.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypePaymentMethod {
    FAST_TRANSFER("FAST_TRANSFER"),
    BANK_TRANSFER("BANK_TRANSFER"),
    PAYMENT_DELIVERY("PAYMENT_DELIVERY");

    private final String value;
}
