package com.abcairline.abc.dto.airport;

import lombok.Data;

import java.util.List;

@Data
public class AirportResultListDto {
    private int count;
    private List<AirportDto> data;
}
