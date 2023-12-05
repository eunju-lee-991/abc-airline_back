package com.abcairline.abc.dto.payment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class PaymentRequest {
    private Long userCouponId;
    @NotNull @NotEmpty
    private String paymentMethod;
}
