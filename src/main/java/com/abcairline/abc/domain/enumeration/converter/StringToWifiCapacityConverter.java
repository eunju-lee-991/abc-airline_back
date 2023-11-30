package com.abcairline.abc.domain.enumeration.converter;

import com.abcairline.abc.domain.enumeration.InFlightMeal;
import com.abcairline.abc.domain.enumeration.WifiCapacity;
import org.springframework.core.convert.converter.Converter;

public class StringToWifiCapacityConverter implements Converter<String, WifiCapacity> {
    @Override
    public WifiCapacity convert(String source) {

        return switch (source) {
            case "1GB" -> WifiCapacity.ONE_GB;
            case "5GB" -> WifiCapacity.FIVE_GB;
            case "10GB" -> WifiCapacity.TEN_GB;
            case "15GB" -> WifiCapacity.FIFTEEN_GB;
            default -> WifiCapacity.NONE;
        };
    }
}
