package com.abcairline.abc.dto.flight;

import com.abcairline.abc.domain.Flight;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class FlightDto {
    private Long id;
    private String departureDate;
    private String departureTime;
    private String arrivalDate;
    private String arrivalTime;
    private String flightNumber;
    private int price;
    public FlightDto(Flight flight) {
        this.id = flight.getId();
        this.departureDate = flight.getDepartureDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.departureTime = flight.getDepartureDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.arrivalDate = flight.getArrivalDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.arrivalTime = flight.getArrivalDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        this.flightNumber = flight.getFlightNumber();
        this.price = flight.getPrice();
    }
}
