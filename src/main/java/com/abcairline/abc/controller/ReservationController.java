package com.abcairline.abc.controller;

import com.abcairline.abc.domain.AncillaryService;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.dto.reservation.*;
import com.abcairline.abc.dto.reservation.ancillary.AncillaryServiceListDto;
import com.abcairline.abc.dto.reservation.temp.TempDataRequest;
import com.abcairline.abc.dto.reservation.temp.TempReservationDto;
import com.abcairline.abc.service.RankingService;
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
@RequestMapping("/api/v1")
public class ReservationController {
    private final ReservationService reservationService;
    private final TempReservationService tempReservationService;
    private final RankingService rankingService;

    // 좌석 지정 페이지 진입
    // /api/flights/{flightId}/seats

    // 부가서비스 페이지
    @GetMapping("/reservations/ancillary-services")
    public AncillaryServiceListDto getAncillaryServices() throws JsonProcessingException {
        return new AncillaryServiceListDto();
    }

    // 예약 저장
    @PostMapping("/users/{userId}/reservations")
    public Long saveReservation(@PathVariable Long userId, CreateReservationRequest request) throws JsonProcessingException {
        Reservation reservation = new Reservation();
        reservation.setAncillaryService(AncillaryService.createAncillaryService(request.getInFlightMeal(), request.getLuggage(), request.getWifi()));
        reservation.setReservationPrice(request.getReservationPrice());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservationDate(LocalDateTime.now());
        reservationService.createReservation(reservation, userId, request.getFlightId(), request.getSeatId());

        // 예약 임시 저장 데이터 삭제
        tempReservationService.deleteTempReservation(userId, request.getFlightId());

        // 예약 순위 데이터 저장 (route를 저장)
        rankingService.recordReservation(reservation.getFlight().getRoute().getId());

        return reservation.getId();
    }

    // one reservation
    @GetMapping("/reservations/{reservationId}")
    public ReservationDto findReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        return new ReservationDto(reservation);
    }

    // all reservation
    @GetMapping("/reservations")
    public SimpleReservationListDto findAllReservation() {
        List<Reservation> reservations = reservationService.retrieveAllReservations();
        SimpleReservationListDto dto = new SimpleReservationListDto();
        dto.setCount(reservations.size());
        dto.setData(reservations.stream().map(SimpleReservationDto::new).collect(Collectors.toList()));

        return dto;
    }

    // user's all reservations
    @GetMapping("/users/{userId}/reservations")
    public ReservationResultListDto findReservationsForUser(@PathVariable Long userId) {
        List<Reservation> reservationList = reservationService.retrieveReservationsForUser(userId);
        ReservationResultListDto result = new ReservationResultListDto();
        result.setCount(reservationList.size());
        result.setData(reservationList.stream().map(ReservationDto::new).collect(Collectors.toList()));

        return result;
    }

    @PutMapping("/reservations/{reservationId}")
    public SimpleReservationDto updateReservation(@PathVariable("reservationId") Long reservationId, UpdateReservationRequest request) {
        AncillaryService ancillaryService = AncillaryService.createAncillaryService(request.getInFlightMeal(), request.getLuggage(), request.getWifi());
        reservationService.updateReservation(reservationId, ancillaryService, request.getSeatId());

        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        return new SimpleReservationDto(reservation);
    }

    @PostMapping("/reservations/{reservationId}/cancel")
    public SimpleReservationDto cancelReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.cancelReservation(reservationId);
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);

        return new SimpleReservationDto(reservation);
    }

    @GetMapping("/users/{userId}/reservations/temp-data")
    public TempReservationDto getTempReservation(@PathVariable Long userId, @RequestParam Long flightId) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>(); // TempData DTO!?
        if (userId != null && flightId != null) {
            map = tempReservationService.getValue(userId, flightId);
        }

        return new TempReservationDto(map);
    }

    @PostMapping("/users/{userId}/reservations/temp-data")
    public void saveTempReservation(@PathVariable Long userId, TempDataRequest request) throws JsonProcessingException {
        if (userId != null && request.getFlightId() != null) {
            Map<String, String> map = new HashMap<>();
            map.put("seatId", String.valueOf(request.getSeatId()));
            map.put("inFlightMeal", request.getInFlightMeal());
            map.put("luggage", request.getLuggage());
            map.put("wifi", request.getWifi());

            tempReservationService.setValue(userId, request.getFlightId(), map);
        }
    }
}
