package com.abcairline.abc.service;

import com.abcairline.abc.domain.*;
import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.LuggageWeight;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.dto.reservation.CreateReservationRequest;
import com.abcairline.abc.dto.reservation.UpdateReservationRequest;
import com.abcairline.abc.exception.InvalidReservationStateException;
import com.abcairline.abc.repository.FlightRepository;
import com.abcairline.abc.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    TempReservationService tempReservationService;

    @Test
    @Transactional
    @Rollback(value = false)
    void testCreateReservation() throws JsonProcessingException {
        Reservation reservation = new Reservation();
        Assertions.assertThat(reservation.getId()).isNull();
        reservation.setAncillaryService(AncillaryService.createAncillaryService("FISH", "TEN_KG", "TEN_GB"));
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setReservationPrice(9900);
        reservation.setStatus(ReservationStatus.PENDING);
        Map<String, String> map = new HashMap<>();
        Long userId = 2L;
        Long flightId = 1L;
        map.put("dummy", "dummy");
        tempReservationService.setValue(userId, flightId, map);
        reservationService.createReservation(reservation, userId, flightId, 10L);
        tempReservationService.deleteTempReservation(userId, flightId);
        System.out.println("========route===========");
        System.out.println(reservation.getFlight().getRoute().getArrival().getIATACode());
        System.out.println(reservation.getFlight().getRoute().getDeparture().getIATACode());

        Assertions.assertThat(reservation.getId()).isNotNull();
        Assertions.assertThat(tempReservationService.getValue(userId, flightId)).isNull();
        System.out.println(reservation.getId());
    }

    @Test
    void testRetrieveReservation() {
        System.out.println("======testRetrieveReservation====");
        Reservation reservation = reservationService.retrieveReservationWithAllInformation(10000L);
        Assertions.assertThat(reservation.getReservationPrice()).isEqualTo(250000);
        Assertions.assertThat(reservation.getFlight().getFlightNumber()).isEqualTo("AA331");
        Assertions.assertThat(reservation.getSeat().getSeatNumber()).isEqualTo("A1");
    }

    @Test
    void testRetrieveAllReservations() {
        System.out.println("======testRetrieveAllReservations====");
        Assertions.assertThat(reservationService.retrieveAllReservations().size()).isEqualTo(4);
    }

    @Test
    void testUpdateReservation() {
        UpdateReservationRequest request = new UpdateReservationRequest();
        Long reservationId = 10003L;
        request.setInFlightMeal("PORK");
        request.setWifi("FIVE_GB");
        request.setLuggage("TWENTY_KG");
        request.setSeatId(13L);
        AncillaryService ancillaryService = AncillaryService.createAncillaryService(request.getInFlightMeal(), request.getLuggage(), request.getWifi());
        reservationService.updateReservation(reservationId, ancillaryService, request.getSeatId());

        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        Assertions.assertThat(reservation.getSeat().getSeatNumber()).isEqualTo("C1");
        Assertions.assertThat(reservation.getSeat().isAvailable()).isEqualTo(false);
        Assertions.assertThat(reservation.getAncillaryService().getLuggage()).isEqualTo(LuggageWeight.TWENTY_KG);
        // 원래 seat_id 는 18
        Assertions.assertThat(flightRepository.findSeat(18L).isAvailable()).isEqualTo(true);
    }

    @Test
    void testCancelReservation() {
        Long reservationId = 10003L;
        reservationService.cancelReservation(reservationId);

        Reservation reservation = reservationService.retrieveReservationWithAllInformation(reservationId);
        Assertions.assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCEL);
        Assertions.assertThat(reservation.getSeat().isAvailable()).isEqualTo(true);

        Long wrongReservationId = 10000L;
        Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(wrongReservationId)).isInstanceOf(InvalidReservationStateException.class);
    }

    @Test
    void testConfirmReservation() {
        // 결제 끝....
        Long reservationId = 10001L;
        reservationService.confirmReservation(reservationId);
        Reservation reservation = reservationService.retrieveReservation(reservationId);
        Assertions.assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);

        Long canceledId = 10003L;
        Assertions.assertThatThrownBy(() -> reservationService.confirmReservation(canceledId))
                .isInstanceOf(InvalidReservationStateException.class);
    }
}