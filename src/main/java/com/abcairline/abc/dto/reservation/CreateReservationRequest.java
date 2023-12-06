package com.abcairline.abc.dto.reservation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateReservationRequest {
    @NotNull
    private Long flightId;

    private String inFlightMeal;
    private String luggage;
    private String wifi;

    @Positive
    private int reservationPrice;
    @NotNull
    private Long seatId;
}
