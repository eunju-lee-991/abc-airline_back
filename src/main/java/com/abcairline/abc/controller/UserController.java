package com.abcairline.abc.controller;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.dto.user.UserInfoDto;
import com.abcairline.abc.service.TempReservationService;
import com.abcairline.abc.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final TempReservationService tempReservationService;
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserInfoDto findOneUser(@PathVariable Long userId) {
        User user = userService.retrieveOneUserWithReservation(userId);
        List<Long> tempReservations = tempReservationService.getTempReservation(userId);
        int tempReservationCount = tempReservations != null ? tempReservations.size() : 0 ;

        return new UserInfoDto(user, tempReservationCount);
    }

    @GetMapping("/")
    public List<User> findAllUsers() {
        return userService.retrieveAllUsers();
    }


    @GetMapping("/{userId}/coupons")
    public void getCoupons(@PathVariable Long userId) throws JsonProcessingException {

    }
}
