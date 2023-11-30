package com.abcairline.abc.domain.enumeration.converter;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import org.springframework.core.convert.converter.Converter;

public class InFlightMealToStringConverter implements Converter<InFlightMeal, String> {

    @Override
    public String convert(InFlightMeal source) {
        return source == null ? "선택안함" :
            switch (source) {
            case CHICKEN -> "닭고기";
            case PORK -> "돼지고기";
            case FISH -> "생선";
            case BEEF -> "소고기";
            case NONE -> "선택안함";
        };
    }
}
