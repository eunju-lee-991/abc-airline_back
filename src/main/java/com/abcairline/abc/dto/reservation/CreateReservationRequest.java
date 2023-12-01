package com.abcairline.abc.dto.reservation;

import lombok.Data;

@Data
public class CreateReservationRequest {
    private Long flightId;
    private String inFlightMeal;
    private String luggage;
    private String wifi;
    private int reservationPrice;
    private Long seatId;
}
