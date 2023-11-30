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

    public static AncillaryService createAncillaryService(Map<String, String> map) {
        String meal = map.get("inFlightMeal");
        String lug = map.get("luggage");
        String wf = map.get("wifi");

        InFlightMeal inflightMeal = StringUtils.hasText(meal) ? InFlightMeal.valueOf(meal) : InFlightMeal.NONE;
        LuggageWeight luggage = StringUtils.hasText(lug) ? LuggageWeight.valueOf(lug) : LuggageWeight.NONE;
        WifiCapacity wifi = StringUtils.hasText(wf) ? WifiCapacity.valueOf(wf) : WifiCapacity.NONE;

        return new AncillaryService(inflightMeal, luggage, wifi);
    }
}
