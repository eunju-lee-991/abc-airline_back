package com.abcairline.abc.dto.reservation.temp;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class TempDataRequest {
    @NotNull
    private Long flightId;

    private Long seatId;

    private String inFlightMeal;

    private String luggage;

    private String wifi;
}
