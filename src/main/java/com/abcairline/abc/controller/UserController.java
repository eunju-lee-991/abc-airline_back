package com.abcairline.abc.controller;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.dto.user.UserInfoDto;
import com.abcairline.abc.service.TempReservationService;
import com.abcairline.abc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
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

//    @GetMapping("/{userId}/discounts")
//    public void getDiscounts(@PathVariable Long userId, @RequestParam Long flightId, @RequestParam Map<String, String> tempDataMap) throws JsonProcessingException {
//
//    }
}
