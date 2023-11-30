package com.abcairline.abc.domain.enumeration.converter;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import org.springframework.core.convert.converter.Converter;

public class StringToInFlightMealConverter implements Converter<String, InFlightMeal> {
    @Override
    public InFlightMeal convert(String source) {

        return switch (source) {
            case "닭고기" -> InFlightMeal.CHICKEN;
            case "돼지고기" -> InFlightMeal.PORK;
            case "생선" -> InFlightMeal.FISH;
            case "소고기" -> InFlightMeal.BEEF;
            default -> InFlightMeal.NONE;
        };
    }
}
