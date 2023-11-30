package com.abcairline.abc.service;

import com.abcairline.abc.domain.AncillaryService;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.domain.Seat;
import com.abcairline.abc.exception.InvalidReservationStateException;
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
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    @Transactional
    // 예약 저장
    public void createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        Seat seat = flightRepository.findSeat(reservation.getSeat().getId());
        seat.reserveSeat();
    }

    // 단건 예약 조회
    public Reservation retrieveReservation(Long id) {
        return reservationRepository.findOne(id);
    }

    // 단건 예약 detail 조회
    public Reservation retrieveReservationWithAllInformation(Long id) {
        return reservationRepository.findOneWithAllInformation(id);
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
    public Integer countAllReservationsForUser(Long userId, ReservationStatus status) {
        return reservationRepository.countAllReservationsForUser(userId, status);
    }

    @Transactional
    // 예약 수정 (결제 대기 상태에서 기내식/좌석만 변경 가능)
    public void updateReservation(Long reservationId, AncillaryService ancillaryService, Long seatId) {
        Reservation findOne = reservationRepository.findOne(reservationId);

        if(findOne.getStatus() == ReservationStatus.PENDING){
            findOne.updateAncillaryService(ancillaryService);
            Seat seat = flightRepository.findSeat(seatId);
            findOne.updateSeat(seat);
        } else {
            log.info("you can not update your reservation");
            throw new InvalidReservationStateException();
        }
    }

    @Transactional
    // 예약 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findOne(reservationId);
        if(reservation.getStatus() == ReservationStatus.PENDING){
            reservation.getSeat().cancelSeat();
            reservation.setStatus(ReservationStatus.CANCEL);
        } else if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            log.error("already confirmed");
            throw new InvalidReservationStateException();
        } else {
            log.error("already canceled");
            throw new InvalidReservationStateException();
        }
    }
}
