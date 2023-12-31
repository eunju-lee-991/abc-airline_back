package com.abcairline.abc.service;

import com.abcairline.abc.domain.*;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.exception.InvalidReservationStateException;
import com.abcairline.abc.exception.NotExistReservationException;
import com.abcairline.abc.exception.ReservationNotExecuteException;
import com.abcairline.abc.exception.UnavailableSeatException;
import com.abcairline.abc.repository.FlightRepository;
import com.abcairline.abc.repository.ReservationRepository;
import com.abcairline.abc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final FlightService flightService;
    private final FlightRepository flightRepository;

    @Transactional
    // 예약 저장
    public void createReservation(Reservation reservation, Long userId, Long flightId, Long seatId) {
        if (userId == null || flightId == null) {
            throw new ReservationNotExecuteException("User Id or Flight Id is empty");
        }

        if (seatId == null) {
            throw new ReservationNotExecuteException("No seat selected");
        }

        User user = userService.retrieveOneUser(userId);
        Flight flight = flightService.retrieveOneFlight(flightId);
        Seat seat = flightService.retrieveOntSeat(seatId);

        if (!seat.isAvailable()) {
            throw new UnavailableSeatException("This seat is already reserved");
        }

        seat.reserveSeat();
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setSeat(seat);

        reservationRepository.save(reservation);
    }

    // 단건 예약 조회
    public Reservation retrieveReservation(Long reservationId) {
        return reservationRepository.findOne(reservationId);
    }

    // 단건 예약 detail 조회
    public Reservation retrieveReservationWithAllInformation(Long reservationId) {
        Reservation findOne = reservationRepository.findOneWithAllInformation(reservationId);
        if (findOne == null) {
            throw new NotExistReservationException("The reservation number does not exist.");
        }
        return findOne;
    }

    // 전체 예약 조회
    public List<Reservation> retrieveAllReservations() {
        return reservationRepository.findAll();
    }

    // 특정 회원 예약 조회
    public List<Reservation> retrieveReservationsForUser(Long userId) {
        return reservationRepository.findAllForUser(userId);
    }

    // 회원 예약 건수 조회

    /**
     * 상태 조건 주기!
     * condition for reservation status
     */
    public Integer countAllReservationsForUser(Long userId, ReservationStatus status) {
        return reservationRepository.countAllReservationsForUser(userId, status);
    }

    @Transactional
    // 예약 수정 (결제 대기 상태에서 기내식/좌석만 변경 가능)
    public void updateReservation(Long reservationId, AncillaryService ancillaryService, Long seatId) {
        Reservation findOne = reservationRepository.findOne(reservationId);

        if (findOne.getStatus() == ReservationStatus.PENDING) {
            findOne.updateAncillaryService(ancillaryService);
            Seat seat = flightRepository.findSeat(seatId);
            if (!seat.isAvailable()) {
                throw new UnavailableSeatException("Unavailable seat");
            }
            findOne.updateSeat(seat);
        } else {
            throw new InvalidReservationStateException("You cannot update your reservation in this status");
        }
    }

    @Transactional
    // 예약 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findOne(reservationId);
        if (reservation.getStatus() == ReservationStatus.PENDING) {
            reservation.getSeat().cancelSeat();
            reservation.setStatus(ReservationStatus.CANCEL);
        } else if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            log.error("Your reservation is already confirmed");
            throw new InvalidReservationStateException();
        } else {
            log.error("Your reservation is already canceled");
            throw new InvalidReservationStateException();
        }
    }

    @Transactional
    // 결제 완료 -> 예약 확정
    public void confirmReservation(Long reservationId) {
        Reservation findOne = reservationRepository.findOne(reservationId);

        if (findOne.getStatus() == ReservationStatus.PENDING) {
            findOne.setStatus(ReservationStatus.CONFIRMED);
        } else {
            throw new InvalidReservationStateException("You cannot confirm your reservation. Check if your status is already confirmed or canceled");
        }
    }
}
