package com.abcairline.abc.controller;

import com.abcairline.abc.domain.Airplane;
import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.Flight;
import com.abcairline.abc.domain.Seat;
import com.abcairline.abc.dto.airport.AirportDto;
import com.abcairline.abc.dto.airport.AirportResultListDto;
import com.abcairline.abc.dto.flight.AirplaneDto;
import com.abcairline.abc.dto.flight.FlightDto;
import com.abcairline.abc.dto.flight.FlightResultListDto;
import com.abcairline.abc.dto.flight.TotalRouteListDto;
import com.abcairline.abc.dto.seat.SeatDto;
import com.abcairline.abc.dto.seat.SeatResultListDto;
import com.abcairline.abc.service.FlightRouteService;
import com.abcairline.abc.service.FlightService;
import com.abcairline.abc.service.TempReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/flights")
@Tag(name = "항공편 API", description = "항공사의 항공편 API")
public class FlightController {
    private final FlightService flightService;
    private final FlightRouteService flightRouteService;

    @GetMapping("/routes")
    @Operation(summary = "전체 노선", description = "운행 중인 전체 노선")
    public TotalRouteListDto findAllRoutes() {
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
    @Operation(summary = "출발 공항", description = "운행 중인 전체 노선 중 출발지 공항 정보")
    public AirportResultListDto findDepartures() {
        List<Airport> airports = flightRouteService.retrieveAllDepartures();
        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    @GetMapping("/arrivals")
    @Operation(summary = "도착 공항", description = "운행 중인 전체 노선 중 도착지 공항 정보")
    public AirportResultListDto findArrivals() {
        List<Airport> airports = flightRouteService.retrieveAllArrivals();

        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    @GetMapping("/arrivals/{IATACode}")
    @Operation(summary = "출발지에 대한 도착 공항", description = "출발지에서 취항하는 도착 공항 정보")
    @Parameter(name = "IATACode", description = "출발지 공항의 IATA 코드")
    public AirportResultListDto findArrivalsForDeparture(@PathVariable(name = "IATACode") String IATACode) {
        List<Airport> airports = flightRouteService.retrieveArrivalsByDeparture(IATACode);

        AirportResultListDto result = new AirportResultListDto();
        result.setCount(airports.size());
        List<AirportDto> dtoList = airports.stream().map(AirportDto::new).collect(Collectors.toList());
        result.setData(dtoList);

        return result;
    }

    @GetMapping("")
    @Operation(summary = "전체 항공편", description = "해당 노선에 대한 운행 항공편")
    @Parameter(name = "departure", description = "출발지 공항의 IATA 코드")
    @Parameter(name = "arrival", description = "도착지 공항의 IATA 코드")
    @Parameter(name = "date", description = "출발 일자(출발 일자로부터 2일 전후의 항공편 조회) yyyyMMdd 형식으로 요청")
    public FlightResultListDto findFlights(@RequestParam(name = "departure") String departure, @RequestParam(name = "arrival") String arrival, @RequestParam(name = "date") String date) {
        log.info("date: {}", date);

        // DateTimeFormatter를 이용한 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime localDateTime = LocalDate.parse(date, formatter).atStartOfDay();
        log.info("localDateTime: {}", localDateTime);

        List<Flight> list = flightService.retrieveFlightsByRoute(departure, arrival, localDateTime);
        FlightResultListDto result = new FlightResultListDto();
        result.setCount(list.size());
        result.setData(list.stream().map(FlightDto::new).collect(Collectors.toList()));

        return result;
    }

    @GetMapping("/{flightId}/seats")
    @Operation(summary = "항공편 좌석 조회", description = "항공편의 전체 좌석 조회")
    @Parameter(name = "flightId", description = "해당 항공편의 번호")
    public SeatResultListDto findSeatsForFlight(@PathVariable(name = "flightId") Long flightId) {
        List<Seat> seatList = flightService.retrieveAllSeatsForFlight(flightId);

        SeatResultListDto result = new SeatResultListDto();
        result.setTotalCount(seatList.size());
        result.setAvailableCount((int) seatList.stream().filter(Seat::isAvailable).count());
        result.setData(seatList.stream().map(SeatDto::new).collect(Collectors.toList()));

        return result;
    }

    @GetMapping("/airplanes")
    @Operation(summary = "비행기 기종 조회", description = "항공사가 보유한 비행기 기종 조회")
    public List<AirplaneDto> airplaneList() {
        List<Airplane> airplanes = flightService.retrieveAllAirplanes();
        return airplanes.stream().map(AirplaneDto::new).collect(Collectors.toList());
    }
}
