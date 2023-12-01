package com.abcairline.abc.dto.reservation.ancillary;

import com.abcairline.abc.domain.AncillaryService;
import com.abcairline.abc.domain.enumeration.converter.InFlightMealToStringConverter;
import com.abcairline.abc.domain.enumeration.converter.LuggageWeightToStringConverter;
import com.abcairline.abc.domain.enumeration.converter.WifiCapacityToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AncillaryServiceStringDto {
    private String inFlightMeal;
    private String luggage;
    private String wifi;

    public AncillaryServiceStringDto(AncillaryService ancillaryService) {
        InFlightMealToStringConverter mealConverter = new InFlightMealToStringConverter();
        LuggageWeightToStringConverter luggageConverter = new LuggageWeightToStringConverter();
        WifiCapacityToStringConverter wifiConverter = new WifiCapacityToStringConverter();

        this.inFlightMeal = mealConverter.convert(ancillaryService.getInFlightMeal());
        this.luggage = luggageConverter.convert(ancillaryService.getLuggage());
        this.wifi = wifiConverter.convert(ancillaryService.getWifi());
    }
}
