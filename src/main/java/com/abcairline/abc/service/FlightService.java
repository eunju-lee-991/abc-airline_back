package com.abcairline.abc.service;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.Flight;
import com.abcairline.abc.domain.FlightRoute;
import com.abcairline.abc.repository.FlightRepository;
import com.abcairline.abc.repository.FlightRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightRouteRepository routeRepository;

    public List<Airport> retrieveAllDepartures() {
        return routeRepository.findAllDepartures();
    }

    public List<Airport> retrieveAllArrivals() {
        return routeRepository.findAllArrivals();
    }

    public List<Airport> retrieveArrivalsByDeparture(String departureCode) {
        return routeRepository.findArrivalByDeparture(departureCode);
    }

    public List<Flight> retrieveFlightsByRoute(String departureCode, String arrivalCode, LocalDateTime searchDate) {
        List<Flight> flightsByRoute = flightRepository.findFlightsByRoute(departureCode, arrivalCode, searchDate);
        return flightsByRoute;
    }
}
