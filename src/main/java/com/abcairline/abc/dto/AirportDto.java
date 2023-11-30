package com.abcairline.abc.dto;

import com.abcairline.abc.domain.Airport;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AirportDto {
    private String IATACode;
    private String name;
    private String continent;
    private String city;

    public AirportDto(Airport airport) {
        this.IATACode = airport.getIATACode();
        this.name = airport.getName();
        this.continent = airport.getContinent();
        this.city = airport.getCity();
    }
}
