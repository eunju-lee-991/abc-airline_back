package com.abcairline.abc.dto.flight;

import com.abcairline.abc.domain.FlightRoute;
import lombok.Data;

@Data
public class SimpleRouteDto {
    private String departureIATACode;
    private String departureCity;
    private String departureImageUrl;
    private String arrivalIATACode;
    private String arrivalCity;
    private String arrivalImageUrl;

    public SimpleRouteDto(FlightRoute route) {
        this.departureIATACode = route.getDeparture().getIATACode();
        this.departureCity = route.getDeparture().getCity();
        this.departureImageUrl = route.getDeparture().getImageUrl();

        this.arrivalIATACode = route.getArrival().getIATACode();
        this.arrivalCity = route.getArrival().getCity();
        this.arrivalImageUrl = route.getArrival().getImageUrl();
    }
}
