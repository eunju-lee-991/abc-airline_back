package com.abcairline.abc.service;

import com.abcairline.abc.domain.Payment;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.UserCoupon;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.exception.InvalidReservationStateException;
import com.abcairline.abc.exception.NotExistReservationException;
import com.abcairline.abc.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PayService {
    private final UserService userService;
    private final ReservationService reservationService;
    private final PayRepository payRepository;

    @Transactional
    public void pay(Payment payment, Long reservationId, Long userCouponId) {
        Reservation reservation = reservationService.retrieveReservation(reservationId);
        if (reservation == null){
            throw new NotExistReservationException();
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new InvalidReservationStateException("Your reservation status is not pending.");
        }
        payment.setOriginalReservationPrice(reservation.getReservationPrice());

        if (userCouponId != null) {
            UserCoupon userCoupon = userService.retrieveOneUserCoupon(userCouponId);
            if(!userCoupon.isUsedYn()){
                userCoupon.useCoupon();
                payment.setUserCoupon(userCoupon);
                payment.setDiscountPrice(userCoupon.getCoupon().getDiscountPrice());
           }else {
                log.warn("already used coupon");
            }
        }

        payment.setPaidPrice(payment.getOriginalReservationPrice() - payment.getDiscountPrice());
        payment.setReservation(reservation);

        payRepository.save(payment);

        if (payment.getId() != null) {
            reservationService.confirmReservation(reservationId);
        }
    }

    public Payment retrieveOnePayment(Long paymentId) {
        return payRepository.findOne(paymentId);
    }
}
