package com.abcairline.abc.service;

import com.abcairline.abc.domain.Payment;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.UserCoupon;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.dto.payment.PaymentDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayServiceTest {
    @Autowired
    PayService payService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;

    @Test
    void testPayWithCoupon() {
        Long reservationId = 10001L;
        Long userCouponId = 100L;

        Payment payment = new Payment();
        payment.setPaymentMethod("card");
        payService.pay(payment, reservationId, userCouponId);

        Reservation reservation = reservationService.retrieveReservation(reservationId);
        UserCoupon userCoupon = userService.retrieveOneUserCoupon(userCouponId);

        Assertions.assertThat(payment.getOriginalReservationPrice()).isEqualTo(reservation.getReservationPrice());
        Assertions.assertThat(payment.getDiscountPrice()).isEqualTo(userCoupon.getCoupon().getDiscountPrice());
        Assertions.assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);

        Payment payment1 = payService.retrieveOnePayment(payment.getId());
        PaymentDto paymentDto = new PaymentDto(payment1);
        Assertions.assertThat(payment1.getUserCoupon().isUsedYn()).isEqualTo(true);
        Assertions.assertThat(paymentDto.getCouponName()).isEqualTo("Signup Coupon");
    }

    @Test
    void testPayWithoutCoupon() {
        Long reservationId = 10001L;
        Reservation reservation = reservationService.retrieveReservation(reservationId);

        Payment payment = new Payment();
        payment.setPaymentMethod("card");
        payService.pay(payment, reservationId, null);

        Assertions.assertThat(payment.getOriginalReservationPrice()).isEqualTo(reservation.getReservationPrice());
        Assertions.assertThat(payment.getPaidPrice()).isEqualTo(reservation.getReservationPrice());
        Assertions.assertThat(payment.getPaidPrice()).isEqualTo(payment.getOriginalReservationPrice());
        Assertions.assertThat(payment.getPaymentMethod()).isEqualTo("card");

        Payment payment1 = payService.retrieveOnePayment(payment.getId());
        PaymentDto paymentDto = new PaymentDto(payment1);
        Assertions.assertThat(paymentDto.getCouponName()).isNull();
    }

    @Test
    void testGetPaymentInfo() {

    }
}