package com.abcairline.abc.dto.seat;

import com.abcairline.abc.dto.reservation.TempReservationDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SeatResultListDto {
    private int totalCount;
    private int availableCount;
    private List<SeatDto> data;
    private TempReservationDto tempDate;
}
