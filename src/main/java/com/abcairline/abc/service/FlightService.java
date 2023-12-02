package com.abcairline.abc.service;

import com.abcairline.abc.domain.*;
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

    public Flight retrieveOneFlight(Long flightId) {
        return flightRepository.findOne(flightId);
    }

    public List<Flight> retrieveFlightsByRoute(String departureCode, String arrivalCode, LocalDateTime searchDate) {
        List<Flight> flightsByRoute = flightRepository.findFlightsByRoute(departureCode, arrivalCode, searchDate);
        return flightsByRoute;
    }

    public Seat retrieveOntSeat(Long seatId) {
        return flightRepository.findSeat(seatId);
    }

    public List<Seat> retrieveAllSeatsForFlight(Long flightId) {
        List<Seat> allSeats = flightRepository.findAllSeats(flightId);
        if (allSeats == null) {
            throw new RuntimeException("something is wrong!!");
        }
        return allSeats;
    }

    public List<Airplane> retrieveAllAirplanes() {
        return flightRepository.findAllAirplanes();
    }
}
