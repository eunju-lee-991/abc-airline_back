package com.abcairline.abc.service;

import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.ReservationStatus;
import com.abcairline.abc.repository.ReservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class TempReservationServiceTest {
    @Autowired
    TempReservationService tempReservationService;
    @Autowired
    ReservationRepository reservationRepository;

    @Test
    void testTemporalSave() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("reservationPrice", "10000");

        tempReservationService.setValue(2L, 2L, map);

        Map<String, String> getValueMap = tempReservationService.getValue(1L, 1L);
        Assertions.assertEquals(10000, Integer.parseInt(getValueMap.get("reservationPrice")));
    }

    @Test
    void testUpdateTemporalSave() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("reservationPrice", "10000");
        tempReservationService.setValue(2L, 11L, map);

        Map<String, String> getValueMap = tempReservationService.getValue(2L, 11L);
        Assertions.assertEquals(10000, Integer.parseInt(getValueMap.get("reservationPrice")));
        Assertions.assertNull(getValueMap.get("seatId"));

        map.put("reservationPrice", "20000");
        map.put("seatId", "19");
        tempReservationService.setValue(2L, 11L, map);
        getValueMap = tempReservationService.getValue(2L, 11L);
        Assertions.assertEquals(20000, Integer.parseInt(getValueMap.get("reservationPrice")));
        Assertions.assertEquals("19", getValueMap.get("seatId"));
    }

    @Test
    @Transactional
    void testReservation() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("inflightMeal", "chicken");
        map.put("luggage", "15kg");
        map.put("reservationPrice", "15000");
        map.put("seatId", "10");

        tempReservationService.setValue(1L, 1L, map);

        Long saved = tempReservationService.save(1L, 1L, ReservationStatus.PENDING);

        Reservation findOne = reservationRepository.findOne(saved);

        Assertions.assertEquals("15kg", findOne.getAncillaryService().getLuggage());
        Assertions.assertEquals(15000, findOne.getReservationPrice());
        Assertions.assertEquals("B4", findOne.getSeat().getSeatNumber());
        Assertions.assertEquals("abc@naver.com", findOne.getUser().getEmail());
        Assertions.assertEquals("AA703", findOne.getFlight().getFlightNumber());
    }

    @Test
    void testGetTempReservationsForUser() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("reservationPrice", "9900");
        tempReservationService.setValue(2L, 1L, map);

        map.put("reservationPrice", "1900");
        tempReservationService.setValue(2L, 1L, map);

        Set<Long> tempReservations = tempReservationService.getTempReservation(2L);
        tempReservations.stream().forEach(l -> {
            try {
                System.out.println(l + " <========");
                System.out.println(tempReservationService.getValue(2L, l).get("reservationPrice"));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Test
    void testDeleteTempReservation() throws JsonProcessingException {
        tempReservationService.deleteTempReservation(2L, 1L);
        Assertions.assertNull(tempReservationService.getValue(2L, 1L));
    }

    @Test
    void testDeleteTempDataAfterCreation() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("reservationPrice", "9900");
        map.put("inflightMeal", "fish");
        map.put("luggage", "25kg");
        map.put("wifi", "10GB");
        map.put("seatId", "2");

        tempReservationService.setValue(2L, 1L, map);
        Long saved = tempReservationService.save(2L, 1L, ReservationStatus.PENDING);
        Reservation reservation = reservationRepository.findOne(saved);

        org.assertj.core.api.Assertions.assertThat(reservation.getSeat().isAvailable()).isEqualTo(false);
        org.assertj.core.api.Assertions.assertThat(reservation.getReservationPrice()).isEqualTo(9900);

        org.assertj.core.api.Assertions.assertThat(tempReservationService.getValue(2L, 1L))
                .isNull();

    }

    @Test
    void testGetAllKeys() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("dummy", "dummy");
        tempReservationService.setValue(1L, 1L, map);
        tempReservationService.setValue(2L, 2L, map);
        tempReservationService.flushAll();

        Assertions.assertNull(tempReservationService.getValue(1L, 1L));
        Assertions.assertNull(tempReservationService.getValue(2L, 2L));
    }
}