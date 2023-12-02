package com.abcairline.abc.service;

import com.abcairline.abc.repository.ReservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class TempReservationServiceTest {
    @Autowired
    TempReservationService tempReservationService;
    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    public void flushAll() {
        tempReservationService.deleteAllTempReservations();
    }

    @Test
    void testTemporalSave() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("seatId", "10");
        map.put("reservationPrice", "10000");

        tempReservationService.setValue(1L, 1L, map);

        Map<String, String> getValueMap = tempReservationService.getValue(1L, 1L);
        Assertions.assertThat(getValueMap.get("seatId")).isEqualTo("10");
        Assertions.assertThat(getValueMap.get("reservationPrice")).isEqualTo("10000");
    }

    @Test
    void testUpdateTemporalSave() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("reservationPrice", "5000");

        tempReservationService.setValue(1L, 1L, map);
        Map<String, String> getValueMap = tempReservationService.getValue(1L, 1L);
        Assertions.assertThat(getValueMap.get("reservationPrice")).isEqualTo("5000");

        map.put("reservationPrice", "999");
        tempReservationService.setValue(1L, 1L, map);
        getValueMap = tempReservationService.getValue(1L, 1L);

        Assertions.assertThat(getValueMap.get("reservationPrice")).isEqualTo("999");
    }

    @Test
    void testGetTempReservationsForUser() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("reservationPrice", "5000");

        tempReservationService.setValue(1L, 1L, map);
        tempReservationService.setValue(1L, 2L, map);
        tempReservationService.setValue(1L, 3L, map);

        List<Long> tempReservations = tempReservationService.getTempReservation(1L);
        int tempReservationCount = tempReservations != null ? tempReservations.size() : 0;
        Assertions.assertThat(tempReservationCount).isEqualTo(3);

        List<Long> tempReservations2 = tempReservationService.getTempReservation(2L);
        int tempReservationCount2 = tempReservations2 != null ? tempReservations2.size() : 0;
        Assertions.assertThat(tempReservationCount2).isEqualTo(0);
    }

    @Test
    void testDeleteTempReservation() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("reservationPrice", "5000");

        tempReservationService.setValue(1L, 5L, map);
        tempReservationService.setValue(1L, 6L, map);

        List<Long> tempReservations = tempReservationService.getTempReservation(1L);
        int tempReservationCount = tempReservations != null ? tempReservations.size() : 0;
        Assertions.assertThat(tempReservationCount).isEqualTo(2);

        tempReservationService.deleteTempReservation(1L, 5L);
        List<Long> tempReservations2 = tempReservationService.getTempReservation(1L);
        int tempReservationCount2 = tempReservations2 != null ? tempReservations2.size() : 0;
        Assertions.assertThat(tempReservationCount2).isEqualTo(1);
        Assertions.assertThat(tempReservations2.get(0)).isEqualTo(6L);
    }

    @Test
    void testDeleteNotExistData() throws JsonProcessingException {
        tempReservationService.deleteTempReservation(999L, 999L);
    }

    @Test
    void testFlushAll() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("dummy", "dummy");
        tempReservationService.setValue(1L, 1L, map);
        tempReservationService.setValue(2L, 2L, map);
        tempReservationService.flushAll();

        Assertions.assertThat(tempReservationService.getValue(1L, 1L)).isNull();
        Assertions.assertThat(tempReservationService.getValue(2L, 2L)).isNull();
    }
}