package com.abcairline.abc.service;

import com.abcairline.abc.domain.AncillaryService;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.ReservationStatus;
import com.abcairline.abc.repository.FlightRepository;
import com.abcairline.abc.repository.ReservationRepository;
import com.abcairline.abc.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TempReservationService {
    // Redis Template
    private final RedisTemplate<String, String> redisTemplate;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 값 저장
    public void setValue(Long userId, Long flightId, Map<String, String> map) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String value = objectMapper.writeValueAsString(map);
        hashOperations.put(String.valueOf(userId), String.valueOf(flightId), value);
    }

    // 값 조회
    public Map<String, String> getValue(String userId, String flightId) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String value = (String) hashOperations.get(userId, flightId);
        return objectMapper.readValue(value, new TypeReference<>() {});
    }

    // 예약을 영구 저장
    @Transactional
    public Long save(Long userId, Long flightId, ReservationStatus reservationStatus) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String value = (String) hashOperations.get(String.valueOf(userId), String.valueOf(flightId));
        Map<String, String> map = objectMapper.readValue(value, new TypeReference<>() {});
        AncillaryService ancillaryService = new AncillaryService(map.get("inflightMeal"), map.get("luggage"), "");

        Reservation reservation = Reservation.createReservation(userRepository.findOne(userId), flightRepository.findOne(flightId), ancillaryService,
                Integer.parseInt(map.get("reservationPrice")), map.get("seatNumber"), reservationStatus);

        reservationRepository.save(reservation);

        return reservation.getId();
    }
}
