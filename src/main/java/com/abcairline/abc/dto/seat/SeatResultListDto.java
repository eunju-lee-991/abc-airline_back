package com.abcairline.abc.dto.seat;

import lombok.Data;

import java.util.List;

@Data
public class SeatResultListDto {
    private int totalCount;
    private int availableCount;
    private List<SeatDto> data;
}
