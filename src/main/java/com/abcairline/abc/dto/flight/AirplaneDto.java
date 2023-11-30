package com.abcairline.abc.dto.flight;

import com.abcairline.abc.domain.Airplane;
import lombok.Data;

@Data
public class AirplaneDto {
    private String airplaneName;
    private String manufacturer;
    private Integer totalSeat;

    public AirplaneDto(Airplane airplane) {
        this.airplaneName = airplane.getModel() + "-" + airplane.getSeries();
        this.manufacturer = airplane.getManufacturer();
        this.totalSeat = airplane.getTotalSeat();
    }
}
