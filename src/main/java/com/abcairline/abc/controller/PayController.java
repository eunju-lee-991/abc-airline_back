package com.abcairline.abc.controller;

import com.abcairline.abc.domain.Payment;
import com.abcairline.abc.dto.payment.PaymentDto;
import com.abcairline.abc.dto.payment.PaymentRequest;
import com.abcairline.abc.exception.InvalidPaymentException;
import com.abcairline.abc.service.PayService;
import com.abcairline.abc.service.ReservationService;
import com.abcairline.abc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/reservations/{reservationId}/payments")
public class PayController {
    private final ReservationService reservationService;
    private final UserService userService;
    private final PayService payService;

    // 결제 정보 전송
    @PostMapping("/")
    public Long payForReservation(@PathVariable Long reservationId, @RequestBody PaymentRequest request) {
        Payment payment = new Payment();
        if (request.getPaymentMethod() == null) {
            throw new InvalidPaymentException();
        }
        payment.setPaymentMethod(request.getPaymentMethod());

        payService.pay(payment, reservationId, request.getUserCouponId());

        return payment.getId();
    }

    // 완료된 결제 정보
    @GetMapping("/{paymentId}")
    public PaymentDto getPaymentInfo(@PathVariable Long reservationId, @PathVariable Long paymentId) {
        Payment payment = payService.retrieveOnePayment(paymentId);

        return new PaymentDto(payment);
    }
}
