package com.abcairline.abc.service;

import com.abcairline.abc.domain.*;
import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.exception.InvalidReservationStateException;
import com.abcairline.abc.repository.FlightRepository;
import com.abcairline.abc.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @Transactional
    @Rollback(value = false)
    @Order(1)
    public void testCreateAndRetrieveReservation() {
        User user = userRepository.findOne(2L);
        Flight flight = flightRepository.findOne(11L);
        Seat seat = flightRepository.findSeat(21L);
        Map<String, String> map = new HashMap<>();
        map.put("inFlightMeal", "fish");
        map.put("luggage", "FIFTEEN_KG");
        map.put("wifi", "FIVE_GB");
        AncillaryService ancillaryService = AncillaryService.createAncillaryService(map);
        Reservation reservation = Reservation.createReservation(user, flight, ancillaryService, 50000, seat, ReservationStatus.CONFIRMED);
        reservationService.createReservation(reservation);
        Long saved = reservation.getId();

        Reservation find = reservationService.retrieveReservation(saved);
        Assertions.assertThat(user).isEqualTo(find.getUser());
        Assertions.assertThat(find.getSeat().getSeatNumber()).isEqualTo("A3");
        Assertions.assertThat(find.getSeat().isAvailable()).isEqualTo(false);
        Assertions.assertThat(find.getFlight().getRoute().getDeparture().getIATACode()).isEqualTo("NRT");
    }

//    @Test
//    @Order(2)
//    public void testRetrieveAllReservations() {
//        reservationService.retrieveAllReservations().stream().forEach(
//                r -> Assertions.assertThat(r.getStatus()).isEqualTo(ReservationStatus.CONFIRMED)
//        );
//
//    }

    @Test
    @Order(2)
    public void testRetrieveReservationsForUser() {
        List<Reservation> reservationFor1 = reservationService.retrieveReservationsForUser(1L);
        List<Reservation> reservationFor2 = reservationService.retrieveReservationsForUser(2L);

        Assertions.assertThat(reservationFor2.get(0).getId()).isEqualTo(1L);

        Assertions.assertThat(reservationFor2.size()).isEqualTo(1);
        Assertions.assertThat(reservationFor1.size()).isEqualTo(2);
    }

    @Test
    @Order(3)
    public void testUpdateReservation() {
        // 10000L CONFIRMED
        Reservation reservation1 = new Reservation();
        reservation1.setId(10000L);
        reservation1.setSeat(flightRepository.findSeat(25L)); // 원래 A1

        Map<String, String> map = new HashMap<>();
        map.put("inFlightMeal", "NONE");
        map.put("luggage", "NONE");
        map.put("wifi", "NONE");

//        AncillaryService ancillaryService = AncillaryService.createAncillaryService(map);

        // it should throw exception
        Assertions.assertThatThrownBy(() -> reservationService.updateReservation(10000L, AncillaryService.createAncillaryService(map), 11L)).isInstanceOf(InvalidReservationStateException.class);

        // 10001L PENDING
        Reservation reservation2 = new Reservation();
        reservation2.setId(10001L);

        Map<String, String> serviceMap = new HashMap<>();

        serviceMap.put("wifi", "NONE");
        AncillaryService ancillaryService = AncillaryService.createAncillaryService(serviceMap);

        reservationService.updateReservation(10001L, ancillaryService, 10L);
        Reservation find = reservationService.retrieveReservation(10001L);
        Assertions.assertThat(find.getAncillaryService().getInFlightMeal()).isEqualTo(InFlightMeal.NONE);
        Assertions.assertThat(find.getSeat().getSeatNumber()).isEqualTo("B4");
        Assertions.assertThat(find.getReservationPrice()).isEqualTo(350000); // 결제 금액은 변하면 안됨
        Assertions.assertThat(flightRepository.findSeat(10L).isAvailable()).isEqualTo(false);
        Assertions.assertThat(find.getSeat().isAvailable()).isEqualTo(false);
       }

    @Test
    @Order(4)
    public void testCancelReservation() {
        Reservation reservation1 = new Reservation();
        reservation1.setId(10000L); // 10000L CONFIRMED

        Reservation reservation2 = new Reservation();
        reservation2.setId(10001L); // 10001L PENDING

        Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(reservation1.getId())).isInstanceOf(InvalidReservationStateException.class);

        reservationService.cancelReservation(reservation2.getId());
        Reservation find = reservationService.retrieveReservation(reservation2.getId());

        Assertions.assertThat(find.getStatus()).isEqualTo(ReservationStatus.CANCEL);
        Assertions.assertThat(find.getSeat().isAvailable()).isEqualTo(true);

        Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(find.getId())).isInstanceOf(InvalidReservationStateException.class);

    }
}