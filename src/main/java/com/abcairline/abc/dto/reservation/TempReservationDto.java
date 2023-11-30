package com.abcairline.abc.dto.reservation;

import lombok.Data;

import java.util.Map;

@Data
public class TempReservationDto {
    private Long seatId;
    private String inFlightMeal;
    private String luggage;
    private String wifi;
    private int discount;

    public TempReservationDto(Map<String, String> map) {
        this.seatId = map.get("seatId") == null ? null : Long.parseLong(map.get("seatId"));
        this.inFlightMeal = map.get("inFlightMeal") == null ? null : map.get("inFlightMeal");
        this.luggage = map.get("luggage") == null ? null : map.get("luggage");
        this.wifi = map.get("wifi") == null ? null : map.get("wifi");
        this.discount = map.get("discount") == null ? 0 : Integer.parseInt(map.get("discount"));
    }
}
