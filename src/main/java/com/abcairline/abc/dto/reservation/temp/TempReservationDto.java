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
        this.seatId = StringUtils.hasText(map.get("seatId")) ? null : Long.parseLong(map.get("seatId"));
        this.inFlightMeal = StringUtils.hasText(map.get("inFlightMeal")) ? null : map.get("inFlightMeal");
        this.luggage = StringUtils.hasText(map.get("luggage")) ? null : map.get("luggage");
        this.wifi = StringUtils.hasText(map.get("wifi"))  ? null : map.get("wifi");
    }
}
