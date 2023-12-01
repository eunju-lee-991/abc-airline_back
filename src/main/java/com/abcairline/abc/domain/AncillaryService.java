package com.abcairline.abc.domain;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.LuggageWeight;
import com.abcairline.abc.domain.enumeration.WifiCapacity;
import com.abcairline.abc.domain.enumeration.converter.StringToInFlightMealConverter;
import com.abcairline.abc.domain.enumeration.converter.StringToLuggageConverter;
import com.abcairline.abc.domain.enumeration.converter.StringToWifiCapacityConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.Map;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AncillaryService {

    @Enumerated(EnumType.STRING)
    private InFlightMeal inFlightMeal;

    @Enumerated(EnumType.STRING)
    private LuggageWeight luggage;

    @Enumerated(EnumType.STRING)
    private WifiCapacity wifi;

    public static AncillaryService createAncillaryService(String inFlightMealStr, String luggageStr, String wifiStr) {
        InFlightMeal inflightMeal = StringUtils.hasText(inFlightMealStr) ? InFlightMeal.valueOf(inFlightMealStr) : InFlightMeal.NONE;
        LuggageWeight luggage = StringUtils.hasText(luggageStr) ? LuggageWeight.valueOf(luggageStr) : LuggageWeight.NONE;
        WifiCapacity wifi = StringUtils.hasText(wifiStr) ? WifiCapacity.valueOf(wifiStr) : WifiCapacity.NONE;

        return new AncillaryService(inflightMeal, luggage, wifi);
    }
}
