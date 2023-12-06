package com.abcairline.abc.service;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.FlightRoute;
import com.abcairline.abc.repository.FlightRouteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightRouteService {
    private final FlightRouteRepository routeRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String ROUTE_KEY = "FLIGHT_ROUTE_KEY";

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

    public void loadRouteDataToRedis() throws JsonProcessingException {
        List<FlightRoute> routes = routeRepository.findAll();
        for (FlightRoute fr : routes) {
            String value = objectMapper.writeValueAsString(fr);
            redisTemplate.opsForHash().put(ROUTE_KEY, String.valueOf(fr.getId()), value);
        }
    }

    public FlightRoute retrieveRouteDataFromRedis(Long routeId) throws JsonProcessingException {
        String value = (String) redisTemplate.opsForHash().get(ROUTE_KEY, String.valueOf(routeId));
        FlightRoute flightRoute = objectMapper.readValue(value, FlightRoute.class);

        return flightRoute;
    }
}
