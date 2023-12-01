package com.abcairline.abc.dto.reservation;

import com.abcairline.abc.domain.AncillaryService;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.enumeration.converter.InFlightMealToStringConverter;
import com.abcairline.abc.domain.enumeration.converter.LuggageWeightToStringConverter;
import com.abcairline.abc.domain.enumeration.converter.WifiCapacityToStringConverter;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
public class ReservationDto {
    private Long id;
    private String email;
    private int reservationPrice;
    private String flightNumber;
    private String departure;
    private String arrival;
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String departureTime;
    private String arrivalDate;
    private String arrivalTime;
    private Map<String, String> ancillaryService;
    private String airplaneModel;
    private String airplaneSeries;
    private String seatNumber;
    private String reservationStatus;

    public ReservationDto(Reservation reservation) {
        InFlightMealToStringConverter mealConverter = new InFlightMealToStringConverter();
        LuggageWeightToStringConverter luggageConverter = new LuggageWeightToStringConverter();
        WifiCapacityToStringConverter wifiConverter = new WifiCapacityToStringConverter();

        this.id = reservation.getId();
        this.email = reservation.getUser().getEmail();
        this.reservationPrice = reservation.getReservationPrice();
        this.flightNumber = reservation.getFlight().getFlightNumber();
        this.departure = reservation.getFlight().getRoute().getDeparture().getCity();
        this.arrival = reservation.getFlight().getRoute().getArrival().getCity();
        this.departureAirport = reservation.getFlight().getRoute().getDeparture().getName();
        this.arrivalAirport = reservation.getFlight().getRoute().getArrival().getName();
        this.departureDate = reservation.getFlight().getDepartureDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.arrivalDate = reservation.getFlight().getArrivalDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.departureTime = reservation.getFlight().getDepartureDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.arrivalTime = reservation.getFlight().getArrivalDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.airplaneModel = reservation.getFlight().getAirplane().getModel();
        this.airplaneSeries = reservation.getFlight().getAirplane().getSeries();
        this.seatNumber = reservation.getSeat().getSeatNumber();
        this.reservationStatus = reservation.getStatus().name();

        if (reservation.getAncillaryService() != null) {
            ancillaryService.put("inFlightMeal", mealConverter.convert(reservation.getAncillaryService().getInFlightMeal()));
            ancillaryService.put("luggage", luggageConverter.convert(reservation.getAncillaryService().getLuggage()));
            ancillaryService.put("wifi", wifiConverter.convert(reservation.getAncillaryService().getWifi()));
       }
    }
}
