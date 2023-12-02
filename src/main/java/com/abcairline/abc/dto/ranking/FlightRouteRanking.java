package com.abcairline.abc.dto.ranking;

import com.abcairline.abc.dto.flight.SimpleRouteDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightRouteRanking {
    private int ranking;
    private SimpleRouteDto routeInfo;
}
