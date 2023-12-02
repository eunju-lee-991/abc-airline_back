package com.abcairline.abc.controller;

import com.abcairline.abc.domain.FlightRoute;
import com.abcairline.abc.dto.flight.SimpleRouteDto;
import com.abcairline.abc.dto.ranking.FlightRouteRanking;
import com.abcairline.abc.service.FlightRouteService;
import com.abcairline.abc.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rankings")
public class RankingController {
    private final RankingService rankingService;
    private final FlightRouteService flightRouteService;

    @GetMapping({"/", ""})
    public List<FlightRouteRanking> getReservationRanking() {
        List<Long> rankingList = rankingService.getReservationRanking();
        List<SimpleRouteDto> simpleRouteDtoList = rankingList.stream().map(routeId -> flightRouteService.retrieveOneRoute(routeId))
                .map(SimpleRouteDto::new).collect(Collectors.toList());

        List<FlightRouteRanking> result = new ArrayList<>();

        for (int i = 0; i < simpleRouteDtoList.size(); i++) {
            result.add(new FlightRouteRanking(i + 1, simpleRouteDtoList.get(i)));
        }

        return result;
    }
}
