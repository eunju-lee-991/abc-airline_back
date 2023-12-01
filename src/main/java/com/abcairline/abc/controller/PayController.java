package com.abcairline.abc.controller;

import com.abcairline.abc.dto.payment.PaymentDto;
import com.abcairline.abc.dto.payment.PaymentRequest;
import com.abcairline.abc.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reservations/{reservationId}/payments")
public class PayController {
    private final ReservationService reservationService;

    // 결제 페이지 진입
    @GetMapping("/")
    public PaymentDto payForReservation(@PathVariable Long reservationId) {
        int reservationPrice = reservationService.retrieveReservation(reservationId).getReservationPrice();

        /**
         *
         *
         */

        return new PaymentDto();
    }

    // 결제 정보 전송
    @PostMapping("/")
    public void payForReservation(@PathVariable Long reservationId, PaymentRequest request) {

        /**
         *
         *
         */

    }

    // 완료된 결제 정보
    @GetMapping("/{paymentId}")
    public void getPaymentInfo(@PathVariable Long reservationId, @PathVariable Long paymentId) {


    }
}
