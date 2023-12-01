package com.abcairline.abc.controller;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.domain.UserCoupon;
import com.abcairline.abc.dto.user.UserCouponDto;
import com.abcairline.abc.dto.user.UserCouponListDto;
import com.abcairline.abc.dto.user.UserInfoDto;
import com.abcairline.abc.service.TempReservationService;
import com.abcairline.abc.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping({"", "/"})
    public List<UserInfoDto> findAllUsers() {
        List<User> users = userService.retrieveAllUsers();
        List<UserInfoDto> result = new ArrayList<>();
        for (User user : users) {
            List<Long> tempReservations = tempReservationService.getTempReservation(user.getId());
            int tempReservationCount = tempReservations != null ? tempReservations.size() : 0 ;

            result.add(new UserInfoDto(user, tempReservationCount));
        }
        return result;
    }

    @GetMapping("/{userId}/coupons")
    public UserCouponListDto getCoupons(@PathVariable Long userId) throws JsonProcessingException {
        List<UserCoupon> userCoupons = userService.retrieveUserCoupons(userId);
        UserCouponListDto result = new UserCouponListDto();
        result.setCount(userCoupons.size());
        result.setData(userCoupons.stream().map(UserCouponDto::new).collect(Collectors.toList()));

        return result;
    }
}
