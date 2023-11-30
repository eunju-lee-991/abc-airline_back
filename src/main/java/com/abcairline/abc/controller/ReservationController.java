package com.abcairline.abc.controller;

import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.dto.reservation.AncillaryServiceDto;
import com.abcairline.abc.dto.reservation.ReservationDto;
import com.abcairline.abc.dto.reservation.ReservationResultListDto;
import com.abcairline.abc.dto.reservation.TempReservationDto;
import com.abcairline.abc.exception.ReservationNotExecuteException;
import com.abcairline.abc.service.ReservationService;
import com.abcairline.abc.service.TempReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public AncillaryServiceDto getAncillaryServices(@PathVariable Long userId, @RequestParam Long flightId, @RequestParam Map<String, String> tempDataMap) throws JsonProcessingException {
        TempReservationDto tempData = null;

        if (userId != null && flightId != null) {
           Map<String, String> getValue = tempReservationService.getValue(userId, flightId);
            tempData = getValue == null ? null : new TempReservationDto(getValue);
        }

        AncillaryServiceDto dto = new AncillaryServiceDto(tempData);
        dto.setTempData(tempData);

        return dto;
    }

    // 할인 페이지 진입
    // /api/users/{userId}/discounts

    // 예약 완료 -> 영구 저장
    @PostMapping("/")
    public Long saveReservation(@PathVariable Long userId, @RequestParam Long flightId) throws JsonProcessingException {
        if (userId == null || flightId == null) {
            throw new ReservationNotExecuteException("no user id or no flight id");
        }

        Map<String, String> getValue = tempReservationService.getValue(userId, flightId);
        if(getValue.get("seatId") == null){
            /**
             * 기내식, 할인은 없어도 OK
             */
            throw new ReservationNotExecuteException("no seat selected");
        }

        return tempReservationService.save(userId, flightId, ReservationStatus.PENDING);
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

    @GetMapping("/temp-save")
    public void saveTempReservation(@PathVariable Long userId, @RequestParam Long flightId, @RequestParam Map<String, String> map) throws JsonProcessingException {
        if (userId != null && flightId != null) {
            tempReservationService.setValue(userId, flightId, map);
        }
    }

    // 결제 페이지 진입
    @GetMapping("/{reservationId}/payment")
    public Integer payForReservation(@PathVariable Long reservationId) {
        int reservationPrice = reservationService.retrieveReservation(reservationId).getReservationPrice();

        return reservationPrice;
    }
}
