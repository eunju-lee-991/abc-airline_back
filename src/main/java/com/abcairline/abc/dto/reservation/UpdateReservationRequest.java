package com.abcairline.abc.dto.reservation;

import lombok.Data;

@Data
public class UpdateReservationRequest {
    private String inFlightMeal;
    private String luggage;
    private String wifi;
    private Long seatId;
}
