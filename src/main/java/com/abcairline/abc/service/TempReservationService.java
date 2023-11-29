package com.abcairline.abc.service;

import com.abcairline.abc.domain.AncillaryService;
import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.ReservationStatus;
import com.abcairline.abc.domain.Seat;
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

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempReservationService {
    // Redis Template
    private final RedisTemplate<String, String> redisTemplate;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final ObjectMapper objectMapper;

    // 값 저장
    public void setValue(Long userId, Long flightId, Map<String, String> map) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        String value = objectMapper.writeValueAsString(map);
        hashOperations.put(String.valueOf(userId), String.valueOf(flightId), value);
    }

    // 값 조회
    public Map<String, String> getValue(Long userId, Long flightId) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        String value = (String) hashOperations.get(String.valueOf(userId), String.valueOf(flightId));
        return value == null ? null : objectMapper.readValue(value, new TypeReference<>() {});
    }

    // userId에 들어있는 fligt 조회
    public Set<Long> getTempReservation(Long userId){
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        return hashOperations.entries(String.valueOf(userId)).keySet().stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    //
    public void deleteTempReservation(Long userId, Long flightId) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        Long delete = hashOperations.delete(String.valueOf(userId), String.valueOf(flightId));
        System.out.println("deleted ====> " + delete);
    }

    // 예약을 영구 저장
    public Long save(Long userId, Long flightId, ReservationStatus reservationStatus) throws JsonProcessingException {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        String value = (String) hashOperations.get(String.valueOf(userId), String.valueOf(flightId));
        Map<String, String> map = objectMapper.readValue(value, new TypeReference<>() {});
        AncillaryService ancillaryService = new AncillaryService(map.get("inflightMeal"), map.get("luggage"), map.get("wifi"));
        Seat seat = flightRepository.findSeat(Long.parseLong(map.get("seatId")));
        seat.reserveSeat();
        Reservation reservation = Reservation.createReservation(userRepository.findOne(userId), flightRepository.findOne(flightId), ancillaryService,
                Integer.parseInt(map.get("reservationPrice")), seat, reservationStatus);

        reservationService.createReservation(reservation);
        deleteTempReservation(userId, flightId);

        return reservation.getId();
    }

}
