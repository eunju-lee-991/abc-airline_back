package com.abcairline.abc.dto.reservation.temp;

import lombok.Data;

@Data
public class TempDataRequest {
    private Long flightId;
    private Long seatId;
    private String inFlightMeal;
    private String luggage;
    private String wifi;
    private int discount;
}
