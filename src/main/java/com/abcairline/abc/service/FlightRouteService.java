package com.abcairline.abc.service;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.FlightRoute;
import com.abcairline.abc.repository.FlightRouteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightRouteService {
    private final FlightRouteRepository routeRepository;

    @Cacheable(value = "oneRouteCache")
    public FlightRoute retrieveOneRoute(Long routeId) {
        return routeRepository.findOne(routeId);
    }

    @Cacheable(value = "departuresCache")
    public List<Airport> retrieveAllDepartures() {
        return routeRepository.findAllDepartures();
    }

    @Cacheable(value = "arrivalsCache")
    public List<Airport> retrieveAllArrivals() {
        return routeRepository.findAllArrivals();
    }


    @Cacheable(value = "arrivalsByDepartureCache")
    public List<Airport> retrieveArrivalsByDeparture(String departureCode) {
        return routeRepository.findArrivalByDeparture(departureCode);
    }

    @Cacheable(value = "allRouteCache")
    public List<FlightRoute> retrieveAllRoutes()  {
        return routeRepository.findAll();
    }
}
