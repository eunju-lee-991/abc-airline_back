package com.abcairline.abc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final StringRedisTemplate redisTemplate;

    private static final String RESERVATION_RANKING_KEY = "reservation_ranking";

    // 예약 저장 시에 route를 record
    public void recordReservation(Long routeId) {
        redisTemplate.opsForZSet().incrementScore(RESERVATION_RANKING_KEY, String.valueOf(routeId), 1);
        redisTemplate.expire(RESERVATION_RANKING_KEY, 3600 * 5, TimeUnit.SECONDS); // 5시간 뒤 사라짐
    }

    // 예약 랭킹 보여주기
    public List<Long> getReservationRanking() {
        Set<String> rankSet = redisTemplate.opsForZSet().reverseRange(RESERVATION_RANKING_KEY, 0, 9);

        return List.copyOf(rankSet).stream().map(Long::parseLong).toList();
    }

    // 예약 순위 삭제
    public void deleteAllReservations() {
        redisTemplate.delete(RESERVATION_RANKING_KEY);
    }

}
