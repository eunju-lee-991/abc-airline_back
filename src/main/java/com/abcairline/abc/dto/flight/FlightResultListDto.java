package com.abcairline.abc.dto.flight;

import lombok.Data;

import java.util.List;

@Data
public class FlightResultListDto {
    private int count;
    private List<FlightDto> data;
}
