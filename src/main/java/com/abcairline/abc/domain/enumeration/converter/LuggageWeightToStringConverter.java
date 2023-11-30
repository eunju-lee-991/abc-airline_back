package com.abcairline.abc.domain.enumeration.converter;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.LuggageWeight;
import org.springframework.core.convert.converter.Converter;

public class LuggageWeightToStringConverter implements Converter<LuggageWeight, String> {

    @Override
    public String convert(LuggageWeight source) {
        return source == null ? "선택안함" :
            switch (source) {
            case TEN_KG -> "10KG";
            case FIFTEEN_KG -> "15KG";
            case TWENTY_KG -> "20KG";
            case TWENTY_FIVE_KG -> "25KG";
            case NONE -> "선택안함";
        };
    }
}
