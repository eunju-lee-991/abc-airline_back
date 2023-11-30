package com.abcairline.abc.domain.enumeration.converter;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.LuggageWeight;
import org.springframework.core.convert.converter.Converter;

public class StringToLuggageConverter implements Converter<String, LuggageWeight> {
    @Override
    public LuggageWeight convert(String source) {

        return switch (source) {
            case "10KG" -> LuggageWeight.TEN_KG;
            case "15KG" -> LuggageWeight.FIFTEEN_KG;
            case "20KG" -> LuggageWeight.TWENTY_KG;
            case "25KG" -> LuggageWeight.TWENTY_FIVE_KG;
            default -> LuggageWeight.NONE;
        };
    }
}
