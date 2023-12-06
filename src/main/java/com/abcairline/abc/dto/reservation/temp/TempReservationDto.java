package com.abcairline.abc.dto.reservation.temp;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Map;

@Data
public class TempReservationDto {
    private Long seatId;
    private String inFlightMeal;
    private String luggage;
    private String wifi;

    public TempReservationDto(Map<String, String> map) {
        this.seatId = StringUtils.hasText(map.get("seatId")) ? Long.parseLong(map.get("seatId")) : null;
        this.inFlightMeal = StringUtils.hasText(map.get("inFlightMeal")) ? map.get("inFlightMeal") : null;
        this.luggage = StringUtils.hasText(map.get("luggage")) ? map.get("luggage") : null;
        this.wifi = StringUtils.hasText(map.get("wifi"))  ? map.get("wifi") : null;
    }
}
