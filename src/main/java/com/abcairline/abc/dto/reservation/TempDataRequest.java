package com.abcairline.abc.dto.reservation;

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
