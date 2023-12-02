package com.abcairline.abc.service;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.FlightRoute;
import com.abcairline.abc.repository.FlightRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightRouteService {
    private final FlightRouteRepository routeRepository;

    public FlightRoute retrieveOneRoute(Long routeId) {
        return routeRepository.findOne(routeId);
    }

    public List<Airport> retrieveAllDepartures() {
        return routeRepository.findAllDepartures();
    }

    public List<Airport> retrieveAllArrivals() {
        return routeRepository.findAllArrivals();
    }

    public List<Airport> retrieveArrivalsByDeparture(String departureCode) {
        return routeRepository.findArrivalByDeparture(departureCode);
    }

}
