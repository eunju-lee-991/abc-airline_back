package com.abcairline.abc.controller;

import com.abcairline.abc.domain.*;
import com.abcairline.abc.dto.airport.AirportDto;
import com.abcairline.abc.dto.airport.AirportResultListDto;
import com.abcairline.abc.dto.flight.AirplaneDto;
import com.abcairline.abc.dto.flight.FlightDto;
import com.abcairline.abc.dto.flight.FlightResultListDto;
import com.abcairline.abc.dto.flight.TotalRouteListDto;
import com.abcairline.abc.dto.seat.SeatDto;
import com.abcairline.abc.dto.seat.SeatResultListDto;
import com.abcairline.abc.repository.FlightRouteRepository;
import com.abcairline.abc.service.FlightRouteService;
import com.abcairline.abc.service.FlightService;
import com.abcairline.abc.service.TempReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights")
public class FlightController {
    private final FlightService flightService;
    private final FlightRouteService flightRouteService;
    private final TempReservationService tempReservationService;

    @GetMapping("/routes")
    public TotalRouteListDto findAllRoutes() { // 운행 중인 전체 노선
        TotalRouteListDto result = new TotalRouteListDto();

        List<Airport> departureAirports = flightRouteService.retrieveAllDepartures();
        result.setDepartures(departureAirports.stream().map(AirportDto::new).collect(Collectors.toList()));
        Map<String, List<AirportDto>> arrivalMap = new HashMap<>();

        int count = 0;
        for (Airport ap : departureAirports) {
            List<Airport> arrivals = flightRouteService.retrieveArrivalsByDeparture(ap.getIATACode());
            count += arrivals.size();
            arrivalMap.put(ap.getIATACode(), arrivals.stream().map(AirportDto::new).collect(Collectors.toList()));
        }
        result.setArrivals(arrivalMap);
        result.setCount(count);

        return result;
    }

    @GetMapping("/departures")
    public AirportResultListDto findDepartures() {
        List<Airport> airports = flightRouteService.retrieveAllDepartures();
        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    @GetMapping("/arrivals")
    public AirportResultListDto findArrivals() {
        List<Airport> airports = flightRouteService.retrieveAllArrivals();

        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    @GetMapping("/arrivals/{IATACode}")
    public AirportResultListDto findArrivalsForDeparture(@PathVariable String IATACode) {
        List<Airport> airports = flightRouteService.retrieveArrivalsByDeparture(IATACode);

        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    // 모든 항공편
    @GetMapping({"", "/"})
    public FlightResultListDto findFlights(String departure, String arrival, String date) {
        System.out.println(date);

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date);
        ZonedDateTime seoulDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        LocalDateTime searchTime = seoulDateTime.toLocalDateTime();

        List<Flight> list = flightService.retrieveFlightsByRoute(departure, arrival, searchTime);
        FlightResultListDto result = new FlightResultListDto();
        result.setCount(list.size());
        result.setData(list.stream().map(FlightDto::new).collect(Collectors.toList()));

        return result;
    }

    @GetMapping("/{flightId}/seats")
    public SeatResultListDto findSeatsForFlight(Long userId, @PathVariable Long flightId, @RequestParam Map<String, String> tempDataMap) throws JsonProcessingException {
        List<Seat> seatList = flightService.retrieveAllSeatsForFlight(flightId);

        SeatResultListDto result = new SeatResultListDto();
        result.setTotalCount(seatList.size());
        result.setAvailableCount((int) seatList.stream().filter(Seat::isAvailable).count());
        result.setData(seatList.stream().map(SeatDto::new).collect(Collectors.toList()));

        return result;
    }

    @GetMapping("/airplanes")
    public List<AirplaneDto> airplaneList() {
        List<Airplane> airplanes = flightService.retrieveAllAirplanes();
        return airplanes.stream().map(AirplaneDto::new).collect(Collectors.toList());
    }
}
