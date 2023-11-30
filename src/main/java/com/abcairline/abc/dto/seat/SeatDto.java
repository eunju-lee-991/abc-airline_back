package com.abcairline.abc.dto.seat;


import com.abcairline.abc.domain.Seat;
import lombok.Data;

@Data
public class SeatDto {
    private Long id;
    private String seatNumber;
    private boolean isAvailable;

    public SeatDto(Seat seat) {
        this.id = seat.getId();
        this.seatNumber = seat.getSeatNumber();
        this.isAvailable = seat.isAvailable();
    }
}
