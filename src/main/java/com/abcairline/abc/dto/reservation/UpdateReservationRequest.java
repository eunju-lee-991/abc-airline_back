package com.abcairline.abc.dto.reservation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateReservationRequest {
    private String inFlightMeal;
    private String luggage;
    private String wifi;
    @NotNull @NotEmpty
    private Long seatId;
}
