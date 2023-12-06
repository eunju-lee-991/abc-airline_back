package com.abcairline.abc;

import com.abcairline.abc.service.FlightRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class AbcApplication {

	private final FlightRouteService routeService;

	public static void main(String[] args) {
		SpringApplication.run(AbcApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData() {
		return args -> {
			routeService.loadRouteDataToRedis();
		};
	}
}
