package com.abcairline.abc.dto.reservation;

import lombok.Data;

import java.util.List;

@Data
public class ReservationResultListDto {
    private int count;
    private List<ReservationDto> data;
}
