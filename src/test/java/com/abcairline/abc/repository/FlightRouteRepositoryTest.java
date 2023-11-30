package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.FlightRoute;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightRouteRepositoryTest {

    @Autowired
    FlightRouteRepository flightRouteRepository;

    @Transactional
    @Test
    void testGetDepartureAirportByContinent() {
        List<Airport> departure = flightRouteRepository.findDepartureByContinent("Oceania");
        departure.stream().forEach(d -> {
            System.out.println(d.getContinent());
            System.out.println(d.getName());
        });
        System.out.println("==============depart===========");
    }

    @Test
    void testGetArrivalAirportByContinent() {
        System.out.println("==============arrival===========");
        List<Airport> arrival = flightRouteRepository.findArrivalByContinent("Oceania");
        arrival.stream().forEach(a -> {
            System.out.println(a.getContinent());
            System.out.println(a.getIATACode());
            System.out.println(a.getName());
        });
    }

    @Test
    void testFindDepartureByArrival() {
        List<Airport> airports = flightRouteRepository.findDepartureByArrival("ADL"); // only ICN
        Assertions.assertThat(airports.size()).isEqualTo(1);
        Assertions.assertThat(airports.get(0).getIATACode()).isEqualTo("ICN");

    }

    @Test
    void testFindArrivalByDeparture() {
        List<Airport> airports = flightRouteRepository.findArrivalByDeparture("DMK"); // only ICN
        Assertions.assertThat(airports.size()).isEqualTo(1);
        Assertions.assertThat(airports.get(0).getIATACode()).isEqualTo("GMP");

    }
}