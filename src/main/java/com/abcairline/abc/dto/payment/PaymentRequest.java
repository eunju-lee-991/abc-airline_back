package com.abcairline.abc.dto.payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long userCouponId;
    private String paymentMethod;
}
