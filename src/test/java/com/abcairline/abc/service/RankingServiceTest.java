package com.abcairline.abc.service;

import com.abcairline.abc.controller.RankingController;
import com.abcairline.abc.dto.ranking.FlightRouteRanking;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RankingServiceTest {
    @Autowired
    RankingService rankingService;
    @Autowired
    FlightRouteService flightRouteService;

    @Test
    void testRecordReservation() {
        rankingService.recordReservation(1L);
        rankingService.recordReservation(1L);
        rankingService.recordReservation(1L);
        rankingService.recordReservation(1L);

        rankingService.recordReservation(2L);
        rankingService.recordReservation(2L);
        rankingService.recordReservation(2L);

        rankingService.recordReservation(3L);
        rankingService.recordReservation(3L);

        List<Long> reservationRanking = rankingService.getReservationRanking();
        Assertions.assertThat(reservationRanking.size()).isEqualTo(3);
        Assertions.assertThat(reservationRanking.get(0)).isEqualTo(1L);
    }

    @Test
    void testDeleteRecordsOfReservation() {
        rankingService.recordReservation(1L);
        rankingService.recordReservation(1L);
        rankingService.deleteAllReservations();

        List<Long> reservationRanking = rankingService.getReservationRanking();
        Assertions.assertThat(reservationRanking.size()).isEqualTo(0);
    }

    @Test
    void testGetReservationRanking() {
        rankingService.recordReservation(1L);
        rankingService.recordReservation(1L);
        rankingService.recordReservation(1L);
        rankingService.recordReservation(1L);

        rankingService.recordReservation(2L);
        rankingService.recordReservation(2L);
        rankingService.recordReservation(2L);

        rankingService.recordReservation(3L);
        rankingService.recordReservation(3L);

        RankingController controller = new RankingController(rankingService, flightRouteService);

        List<FlightRouteRanking> result = controller.getReservationRanking();

        FlightRouteRanking ranking1 = result.get(0);
        Assertions.assertThat(ranking1.getRanking()).isEqualTo(1);
        Assertions.assertThat(ranking1.getRouteInfo().getDepartureIATACode()).isEqualTo("ICN");
        Assertions.assertThat(ranking1.getRouteInfo().getArrivalIATACode()).isEqualTo("ADL");

    }
}