package com.abcairline.abc.service;

import com.abcairline.abc.repository.FlightRepository;
import com.abcairline.abc.repository.ReservationRepository;
import com.abcairline.abc.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempReservationService {
    // Redis Template
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    // 값 저장
    public void setValue(Long userId, Long flightId, Map<String, String> map) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        if (map != null) {
            String value = objectMapper.writeValueAsString(map);
            hashOperations.put(String.valueOf(userId), String.valueOf(flightId), value);
            redisTemplate.expire(String.valueOf(userId), 3600, TimeUnit.SECONDS); // 1시간 유효기간
        }
    }

    // 값 조회
    public Map<String, String> getValue(Long userId, Long flightId) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        String value = (String) hashOperations.get(String.valueOf(userId), String.valueOf(flightId));
        return value == null ? null : objectMapper.readValue(value, new TypeReference<>() {});
    }

    // userId에 들어있는 fligt 조회
    public List<Long> getTempReservation(Long userId) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<Object, Object> entries = userId != null ? hashOperations.entries(String.valueOf(userId))
                : new HashMap<>();

        return entries.isEmpty() ? null : entries.keySet().stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public void deleteTempReservation(Long userId, Long flightId) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        Long delete = hashOperations.delete(String.valueOf(userId), String.valueOf(flightId));
        System.out.println("deleted ====> " + delete);
    }

    public void deleteAllTempReservations() {
        Set<String> keys = redisTemplate.keys("*[0-9]*");
        System.out.println("====flush all=====");
        keys.forEach(k -> System.out.println(k));

        redisTemplate.delete(keys);
    }
}
