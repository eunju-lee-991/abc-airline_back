package com.abcairline.abc.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long userCouponId;
    @NotBlank
    private String paymentMethod;
}
