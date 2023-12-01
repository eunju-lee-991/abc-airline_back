package com.abcairline.abc.dto.payment;

import com.abcairline.abc.domain.Payment;
import lombok.Data;

@Data
public class PaymentDto {
    private Long paymentId;
    private int originalReservationPrice;
    private Long couponId;
    private String couponName;
    private int discountPrice;
    private int paidPrice;
    private String paymentMethod;

    public PaymentDto(Payment payment) {
        this.paymentId = payment.getId();
        this.originalReservationPrice = payment.getOriginalReservationPrice();
        if(payment.getUserCoupon() != null){
            this.couponId = payment.getUserCoupon().getId();
            this.couponName = payment.getUserCoupon().getCoupon().getCouponName();
        }
        this.discountPrice = payment.getDiscountPrice();
        this.paidPrice = payment.getPaidPrice();
        this.paymentMethod = payment.getPaymentMethod();
    }
}
