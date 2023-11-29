package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Flight;
import com.abcairline.abc.domain.Seat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightRepositoryTest {
    @Autowired
    FlightRepository flightRepository;

    @Test
    void testGetAllSeats() {
        List<Seat> allSeats = flightRepository.findAllSeats(11L);
        Assertions.assertThat(allSeats.size()).isEqualTo(18);
    }

    @Test
    void testGetFlightsByRoute() {
        List<Flight> flights = flightRepository.findFlightsByRoute("NRT", "ICN", null);
        Assertions.assertThat(flights.size()).isEqualTo(2);
        flights.forEach(f -> System.out.println(f.getFlightNumber()));
    }
}