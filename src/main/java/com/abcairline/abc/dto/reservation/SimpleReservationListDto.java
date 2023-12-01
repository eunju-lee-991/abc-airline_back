package com.abcairline.abc.dto.reservation;

import lombok.Data;

import java.util.List;

@Data
public class SimpleReservationListDto {
    private int count;
    private List<SimpleReservationDto> data;
}
