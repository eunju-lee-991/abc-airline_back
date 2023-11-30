package com.abcairline.abc.dto.reservation;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.LuggageWeight;
import com.abcairline.abc.domain.enumeration.WifiCapacity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AncillaryServiceDto {
    private Map<String, String> inFlightMeal;
    private Map<String, String> luggage;
    private Map<String, String> wifi;
    private TempReservationDto tempData;

    public AncillaryServiceDto() {
        setServices();
    }

    public AncillaryServiceDto(TempReservationDto tempData) {
        setServices();
        this.tempData = tempData;
    }

    private void setServices() {
        Map<String, String> mealMap = new HashMap<>();
        mealMap.put(InFlightMeal.CHICKEN.name(), "닭고기");
        mealMap.put(InFlightMeal.PORK.name(), "돼지고기");
        mealMap.put(InFlightMeal.FISH.name(), "생선");
        mealMap.put(InFlightMeal.BEEF.name(), "소고기");
        mealMap.put(InFlightMeal.NONE.name(), "선택안함");
        this.inFlightMeal = mealMap;

        Map<String, String> luggageMap = new HashMap<>();
        luggageMap.put(LuggageWeight.NONE.name(), "선택안함");
        luggageMap.put(LuggageWeight.TEN_KG.name(), "10KG");
        luggageMap.put(LuggageWeight.FIFTEEN_KG.name(), "15KG");
        luggageMap.put(LuggageWeight.TWENTY_KG.name(), "20KG");
        luggageMap.put(LuggageWeight.TWENTY_FIVE_KG.name(), "25KG");
        this.luggage = luggageMap;

        Map<String, String> wifiMap = new HashMap<>();
        wifiMap.put(WifiCapacity.NONE.name(), "선택안함");
        wifiMap.put(WifiCapacity.ONE_GB.name(), "1GB");
        wifiMap.put(WifiCapacity.FIVE_GB.name(), "5GB");
        wifiMap.put(WifiCapacity.TEN_GB.name(), "10GB");
        wifiMap.put(WifiCapacity.FIFTEEN_GB.name(), "15GB");
        this.wifi = wifiMap;
    }
}
