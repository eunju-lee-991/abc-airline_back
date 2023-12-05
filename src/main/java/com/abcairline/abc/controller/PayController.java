package com.abcairline.abc.controller;

import com.abcairline.abc.domain.Payment;
import com.abcairline.abc.dto.payment.PaymentDto;
import com.abcairline.abc.dto.payment.PaymentRequest;
import com.abcairline.abc.exception.InvalidPaymentException;
import com.abcairline.abc.service.PayService;
import com.abcairline.abc.service.ReservationService;
import com.abcairline.abc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/reservations/{reservationId}/payments")
@Tag(name = "결제 API", description = "결제 요청 및 결제 정보 조회 API")
public class PayController {
    private final ReservationService reservationService;
    private final UserService userService;
    private final PayService payService;

    @PostMapping("/")
    @Operation(summary = "예약 결제", description = "결제 정보를 받아 예약 결제/확정")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true
            , description = "사용자 쿠폰과 결제 수단")
    public ResponseEntity<Long> payForReservation(@PathVariable Long reservationId, @Valid @RequestBody PaymentRequest request) {
        Payment payment = new Payment();
        if (request.getPaymentMethod() == null) {
            throw new InvalidPaymentException();
        }
        payment.setPaymentMethod(request.getPaymentMethod());

        payService.pay(payment, reservationId, request.getUserCouponId());

        if (payment.getId() != null) {
            reservationService.confirmReservation(reservationId);
        }

        return new ResponseEntity<>(payment.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "결제 정보", description = "완료된 결제에 대한 정보")
    public PaymentDto getPaymentInfo(@PathVariable Long paymentId) {
        Payment payment = payService.retrieveOnePayment(paymentId);

        return new PaymentDto(payment);
    }
}
