package com.abcairline.abc.controller;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.domain.UserCoupon;
import com.abcairline.abc.dto.user.UserCouponDto;
import com.abcairline.abc.dto.user.UserCouponListDto;
import com.abcairline.abc.dto.user.UserInfoDto;
import com.abcairline.abc.service.TempReservationService;
import com.abcairline.abc.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "사용자 API", description = "사용자 정보 조회 API")
public class UserController {
    private final TempReservationService tempReservationService;
    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "특정 사용자 조회", description = "기본 사용자 정보와 사용자의 예약 관련 카운트 조회")
    public UserInfoDto findOneUser(@PathVariable(name = "userId") Long userId) {
        User user = userService.retrieveOneUser(userId);
        List<Long> tempReservations = tempReservationService.getTempReservation(userId);
        int tempReservationCount = tempReservations != null ? tempReservations.size() : 0 ;

        return new UserInfoDto(user, tempReservationCount);
    }

    @GetMapping("")
    @Operation(summary = "전체 사용자 조회", description = "전체 사용자 정보 조회")
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
    @Operation(summary = "사용자 쿠폰 조회", description = "사용자가 보유한 할인 쿠폰 조회")
    public UserCouponListDto getCoupons(@PathVariable(name = "userId") Long userId) {
        List<UserCoupon> userCoupons = userService.retrieveUserCoupons(userId);
        UserCouponListDto result = new UserCouponListDto();
        result.setCount(userCoupons.size());
        result.setData(userCoupons.stream().map(UserCouponDto::new).collect(Collectors.toList()));

        return result;
    }
}
