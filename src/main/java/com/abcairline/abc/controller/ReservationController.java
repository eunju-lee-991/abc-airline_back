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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "항공편 예약 API", description = "항공편 예약 관련 API")
public class ReservationController {
    private final ReservationService reservationService;
    private final TempReservationService tempReservationService;
    private final RankingService rankingService;

    // 좌석 지정 페이지 진입
    // /api/flights/{flightId}/seats

    // 부가서비스 페이지
    @GetMapping("/reservations/ancillary-services")
    @Operation(summary = "부가서비스 목록", description = "항공사에서 제공하는 부가서비스(기내식/수하물/와이파이) 목록 조회")
    public AncillaryServiceListDto getAncillaryServices() throws JsonProcessingException {
        return new AncillaryServiceListDto();
    }

    // 예약 저장
    @PostMapping("/users/{userId}/reservations")
    @Operation(summary = "예약", description = "예약 저장/임시 저장된 예약 데이터 삭제/실시간 예약 순위 데이터 반영")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true
            , description = "항공편 번호/부가서비스/예약 금액/좌석 번호")
    public ResponseEntity<Long> saveReservation(@PathVariable Long userId, @Valid CreateReservationRequest request) throws JsonProcessingException {
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

        return new ResponseEntity<>(reservation.getId(), HttpStatus.CREATED);
    }

    // one reservation
    @GetMapping("/reservations/{reservationId}")
    @Operation(summary = "예약 단건 조회", description = "공항 및 비행기 기종을 포함한 구체적인 예약 정보 조회")
    public ReservationDto findReservation(@PathVariable(name = "reservationId") Long reservationId) {
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        return new ReservationDto(reservation);
    }

    // all reservation
    @GetMapping("/reservations")
    @Operation(summary = "전체 예약 조회", description = "전체 예약 정보 조회")
    public SimpleReservationListDto findAllReservation() {
        List<Reservation> reservations = reservationService.retrieveAllReservations();
        SimpleReservationListDto dto = new SimpleReservationListDto();
        dto.setCount(reservations.size());
        dto.setData(reservations.stream().map(SimpleReservationDto::new).collect(Collectors.toList()));

        return dto;
    }

    // user's all reservations
    @GetMapping("/users/{userId}/reservations")
    @Operation(summary = "특정 사용자의 전체 예약 조회", description = "특정 사용자의 전체 예약 정보 조회")
    public ReservationResultListDto findReservationsForUser(@PathVariable(name = "userId") Long userId) {
        List<Reservation> reservationList = reservationService.retrieveReservationsForUser(userId);
        ReservationResultListDto result = new ReservationResultListDto();
        result.setCount(reservationList.size());
        result.setData(reservationList.stream().map(ReservationDto::new).collect(Collectors.toList()));

        return result;
    }

    @PutMapping("/reservations/{reservationId}")
    @Operation(summary = "예약 변경", description = "결제 대기 중인 예약 건의 부가서비스/좌석 변경")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "부가서비스/좌석 정보")
    public SimpleReservationDto updateReservation(@PathVariable("reservationId") Long reservationId, @Valid UpdateReservationRequest request) {
        AncillaryService ancillaryService = AncillaryService.createAncillaryService(request.getInFlightMeal(), request.getLuggage(), request.getWifi());
        reservationService.updateReservation(reservationId, ancillaryService, request.getSeatId());

        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        return new SimpleReservationDto(reservation);
    }

    @PostMapping("/reservations/{reservationId}/cancel")
    @Operation(summary = "예약 취소", description = "결제 대기 중인 예약 건의 취소")
    public SimpleReservationDto cancelReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.cancelReservation(reservationId);
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);

        return new SimpleReservationDto(reservation);
    }

    @GetMapping("/users/{userId}/reservations/temp-data")
    @Operation(summary = "진행 중인 예약 조회", description = "임시 저장된 예약 건의 부가서비스/좌석 데이터 조회")
    @Parameter(name = "flightId", description = "예약 진행 중인 항공편 번호")
    public TempReservationDto getTempReservation(@PathVariable(name = "userId") Long userId, @RequestParam(name = "flightId") Long flightId) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        if (userId != null && flightId != null) {
            map = tempReservationService.getValue(userId, flightId);
        }

        return new TempReservationDto(map);
    }

    @PostMapping("/users/{userId}/reservations/temp-data")
    @Operation(summary = "진행 중인 예약 저장", description = "해당 항공편에 대한 부가서비스/좌석 임시 저장")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "항공편 번호/부가서비스/좌석")
    public void saveTempReservation(@PathVariable(name = "userId") Long userId, @Valid @RequestBody TempDataRequest request) throws JsonProcessingException {
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
