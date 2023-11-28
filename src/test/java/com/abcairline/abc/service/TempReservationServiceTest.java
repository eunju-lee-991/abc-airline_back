package com.abcairline.abc.service;

import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.ReservationStatus;
import com.abcairline.abc.repository.ReservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

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
        tempReservationService.setValue(2L, 2L, map);

        Map<String, String> getValueMap = tempReservationService.getValue(2L, 2L);
        Assertions.assertEquals(10000, Integer.parseInt(getValueMap.get("reservationPrice")));
        Assertions.assertNull(getValueMap.get("seatNumber"));

        map.put("reservationPrice", "20000");
        map.put("seatNumber", "A1");
        tempReservationService.setValue(2L, 2L, map);
        getValueMap = tempReservationService.getValue(2L, 2L);
        Assertions.assertEquals(20000, Integer.parseInt(getValueMap.get("reservationPrice")));
        Assertions.assertEquals("A1", getValueMap.get("seatNumber"));
    }

    @Test
    @Transactional
    void testReservation() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("inflightMeal", "chicken");
        map.put("luggage", "15kg");
        map.put("reservationPrice", "15000");
        map.put("seatNumber", "C1");

        tempReservationService.setValue(1L, 1L, map);

        Long saved = tempReservationService.save(1L, 1L, ReservationStatus.PENDING);

        Reservation findOne = reservationRepository.findOne(saved);

        Assertions.assertEquals("15kg", findOne.getAncillaryService().getLuggage());
        Assertions.assertEquals(15000, findOne.getReservationPrice());
        Assertions.assertEquals("C1", findOne.getSeatNumber());
        Assertions.assertEquals("abc@naver.com", findOne.getUser().getEmail());
        Assertions.assertEquals("AA703", findOne.getFlight().getFlightNumber());
    }
}