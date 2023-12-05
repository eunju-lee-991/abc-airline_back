package com.abcairline.abc.controller;

import com.abcairline.abc.domain.FlightRoute;
import com.abcairline.abc.dto.flight.SimpleRouteDto;
import com.abcairline.abc.dto.ranking.FlightRouteRanking;
import com.abcairline.abc.service.FlightRouteService;
import com.abcairline.abc.service.RankingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "실시간 예약 순위 API", description = "실시간 예약 순위 조회 API")
public class RankingController {
    private final RankingService rankingService;
    private final FlightRouteService flightRouteService;

    @GetMapping({"/", ""})
    @Operation(summary = "실시간 예약 순위", description = "1~10위까지의 예약 순위에 해당하는 노선 정보 조회 ")
    public List<FlightRouteRanking> getReservationRanking()  {
        List<Long> rankingList = rankingService.getReservationRanking();
        List<SimpleRouteDto> simpleRouteDtoList = rankingList.stream().map(routeId -> {
                    try {
                        return flightRouteService.retrieveRouteDateFromRedis(routeId);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(SimpleRouteDto::new).collect(Collectors.toList());

        List<FlightRouteRanking> result = new ArrayList<>();

        for (int i = 0; i < simpleRouteDtoList.size(); i++) {
            result.add(new FlightRouteRanking(i + 1, simpleRouteDtoList.get(i)));
        }

        return result;
    }
}
