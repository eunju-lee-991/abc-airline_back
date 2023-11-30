package com.abcairline.abc.controller;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.dto.reservation.TempReservationDto;
import com.abcairline.abc.dto.user.UserInfoDto;
import com.abcairline.abc.service.TempReservationService;
import com.abcairline.abc.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final TempReservationService tempReservationService;
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserInfoDto findOneUser(@PathVariable Long userId) {
        User user = userService.retrieveOneUser(userId);

        int tempReservationCount = tempReservationService.getTempReservation(userId).size();
    }

    @GetMapping("/")
    public List<User> findAllUsers() {
        return userService.retrieveAllUsers();
    }
//
//    @PostMapping("/")
//    public Long saveUser(User user) {
//
//        return userService.saveUser(user);
//    }
//
//    @PutMapping("/{userId}")
//    public Long updateUser(User user) {
//
//        return userService.updateUser(user);
//    }

    @GetMapping("/{userId}/discounts")
    public void getDiscounts(@PathVariable Long userId, @RequestParam Long flightId, @RequestParam Map<String, String> tempDataMap) throws JsonProcessingException {

        TempReservationDto tempData = null;
        if (userId != null && flightId != null) {
            if (tempDataMap != null) {
                tempReservationService.setValue(userId, flightId, tempDataMap);
            }

            Map<String, String> getValue = tempReservationService.getValue(userId, flightId);
            tempData = new TempReservationDto(getValue);
        }

    }
}
