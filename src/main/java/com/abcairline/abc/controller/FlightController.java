package com.abcairline.abc.controller;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.Flight;
import com.abcairline.abc.dto.AirportDto;
import com.abcairline.abc.dto.AirportResultListDto;
import com.abcairline.abc.dto.FlightDto;
import com.abcairline.abc.dto.FlightResultListDto;
import com.abcairline.abc.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;

    @GetMapping("/departures")
    public AirportResultListDto getDepartures() {
        List<Airport> airports = flightService.retrieveAllDepartures();
        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    @GetMapping("/arrivals")
    public AirportResultListDto getArrivals(String departure) {
        List<Airport> airports = StringUtils.hasText(departure) ? flightService.retrieveArrivalsByDeparture(departure)
                : flightService.retrieveAllArrivals();

        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    @GetMapping({"", "/"})
    public FlightResultListDto getFlights(String departure, String arrival, String date) {
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
}
