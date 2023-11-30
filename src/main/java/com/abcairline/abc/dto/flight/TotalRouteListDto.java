package com.abcairline.abc.dto.flight;

import com.abcairline.abc.dto.airport.AirportDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TotalRouteListDto {

    private int count;
    private List<AirportDto> departures;
    private Map<String, List<AirportDto>> arrivals;
}
