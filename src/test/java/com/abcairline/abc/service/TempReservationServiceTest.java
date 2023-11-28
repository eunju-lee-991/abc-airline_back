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
    void testEqualsMap() throws JsonProcessingException {
        Map<String, String> setMap = new HashMap<>();
        setMap.put("test", "test1");
        tempReservationService.setValue(1L, 1L, setMap);
        Map<String, String> getMap = tempReservationService.getValue("1", "1");

        Assertions.assertEquals(setMap, getMap);
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