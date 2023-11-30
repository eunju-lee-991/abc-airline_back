package com.abcairline.abc.domain.enumeration.converter;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.WifiCapacity;
import org.springframework.core.convert.converter.Converter;

public class WifiCapacityToStringConverter implements Converter<WifiCapacity, String> {

    @Override
    public String convert(WifiCapacity source) {
        return source == null ? "선택안함" :
            switch (source) {
            case FIVE_GB -> "5GB";
            case ONE_GB -> "1GB";
            case TEN_GB -> "10GB";
            case FIFTEEN_GB -> "15GB";
            case NONE -> "선택안함";
        };
    }
}
