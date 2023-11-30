package com.abcairline.abc.configuration;

import com.abcairline.abc.domain.enumeration.converter.InFlightMealToStringConverter;
import com.abcairline.abc.domain.enumeration.converter.StringToInFlightMealConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToInFlightMealConverter());
        registry.addConverter(new InFlightMealToStringConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5000")
                .allowCredentials(true)
                .allowedMethods("OPTIONS", "GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*");
    }
}
