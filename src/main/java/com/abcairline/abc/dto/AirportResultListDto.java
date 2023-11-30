package com.abcairline.abc.dto;

import com.abcairline.abc.domain.Airport;
import lombok.Data;

import java.util.List;

@Data
public class DepartureAirportDto {
    private int count;
    private List<AirportDto> data;
}
