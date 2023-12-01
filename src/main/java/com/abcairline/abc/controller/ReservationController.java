package com.abcairline.abc.controller;

import com.abcairline.abc.domain.AncillaryService;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.dto.reservation.*;
import com.abcairline.abc.dto.reservation.ancillary.AncillaryServiceListDto;
import com.abcairline.abc.dto.reservation.temp.TempDataRequest;
import com.abcairline.abc.dto.reservation.temp.TempReservationDto;
import com.abcairline.abc.exception.ReservationNotExecuteException;
import com.abcairline.abc.service.ReservationService;
import com.abcairline.abc.service.TempReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final TempReservationService tempReservationService;

    // 좌석 지정 페이지 진입
    // /api/flights/{flightId}/seats

    // 부가서비스 페이지
    @GetMapping("/ancillary-services")
    public AncillaryServiceListDto getAncillaryServices(@PathVariable Long userId, @RequestParam Long flightId, @RequestParam Map<String, String> tempDataMap) throws JsonProcessingException {
        return new AncillaryServiceListDto();
    }

    // 할인 페이지 진입
    // /api/users/{userId}/discounts

    // 예약 저장
    @PostMapping("/")
    public Long saveReservation(@PathVariable Long userId, CreateReservationRequest request) throws JsonProcessingException {
        if (userId == null || request.getFlightId() == null) {
            throw new ReservationNotExecuteException("no user id or no flight id");
        }

        if(request.getSeatId() == null){
            throw new ReservationNotExecuteException("no seat selected");
        }

        Reservation reservation = new Reservation();
        reservation.setAncillaryService(AncillaryService.createAncillaryService(request.getInFlightMeal(), request.getLuggage(), request.getWifi()));
        reservation.setReservationPrice(request.getReservationPrice());
        reservation.setReservationDate(LocalDateTime.now());
        reservationService.createReservation(reservation, request.getFlightId(), request.getSeatId());

        // 예약 임시 저장 데이터 삭제
        tempReservationService.deleteTempReservation(userId, request.getFlightId());

        return reservation.getId();
    }

    // one reservation
    @GetMapping("/{reservationId}")
    public ReservationDto findReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        return new ReservationDto(reservation);
    }

    // user's all reservations
    @GetMapping("/")
    public ReservationResultListDto findReservationsForUser(@PathVariable Long userId) {
        List<Reservation> reservationList = reservationService.retrieveReservationsForUser(userId);
        ReservationResultListDto result = new ReservationResultListDto();
        result.setCount(reservationList.size());
        result.setData(reservationList.stream().map(ReservationDto::new).collect(Collectors.toList()));

        return result;
    }

    @PutMapping("/{reservationId}")
    public SimpleReservationDto updateReservation(@PathVariable("reservationId") Long reservationId, UpdateReservationRequest request) {
        AncillaryService ancillaryService = AncillaryService.createAncillaryService(request.getInFlightMeal(), request.getLuggage(), request.getWifi());
        reservationService.updateReservation(reservationId, ancillaryService, request.getSeatId());
//        Reservation reservation = reservationService.retrieveReservation(reservationId);
// retrieveReservation 로도 해보기
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        return new SimpleReservationDto(reservation);
    }

    @PostMapping("/{reservationId}")
    public SimpleReservationDto cancelReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.cancelReservation(reservationId);
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);

        return new SimpleReservationDto(reservation);
    }

    @GetMapping("/temp-data")
    public TempReservationDto getTempReservation(@PathVariable Long userId, @RequestParam Long flightId) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>(); // TempData DTO!?
        if (userId != null && flightId != null) {
            map = tempReservationService.getValue(userId, flightId);
        }

        return new TempReservationDto(map);
    }

    @PostMapping("/temp-save")
    public void saveTempReservation(@PathVariable Long userId, TempDataRequest request) throws JsonProcessingException {
        if (userId != null && request.getFlightId() != null) {
            Map<String, String> map = new HashMap<>();
            map.put("seatId", String.valueOf(request.getSeatId()));
            map.put("inFlightMeal", request.getInFlightMeal());
            map.put("luggage", request.getLuggage());
            map.put("wifi", request.getWifi());
            map.put("discount", String.valueOf(request.getDiscount()));

            tempReservationService.setValue(userId, request.getFlightId(), map);
        }
    }

    // 결제 페이지 진입
    @GetMapping("/{reservationId}/payment")
    public Integer payForReservation(@PathVariable Long reservationId) {
        int reservationPrice = reservationService.retrieveReservation(reservationId).getReservationPrice();

        /**
         *
         *
         */

        return reservationPrice;
    }
}
