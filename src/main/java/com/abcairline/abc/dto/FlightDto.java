package com.abcairline.abc.dto;

import com.abcairline.abc.domain.Flight;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDto {
    private Long id;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String flightNumber;
    private int price;
    public FlightDto(Flight flight) {
        this.id = flight.getId();
        this.departureDate = flight.getDepartureDate();
        this.arrivalDate = flight.getArrivalDate();
        this.flightNumber = flight.getFlightNumber();
        this.price = flight.getPrice();
    }
}
