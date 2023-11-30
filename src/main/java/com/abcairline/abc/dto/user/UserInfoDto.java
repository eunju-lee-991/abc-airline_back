package com.abcairline.abc.dto.user;

import com.abcairline.abc.dto.reservation.ReservationDto;

import java.util.List;

public class UserInfoDto {
    private String id;
    private String email;
    private String signUpDate;
    private int tempReservationCount;
    private int confirmedReservationCount;
    private int pendingReservationCount;
    private int canceledReservationCount;
}
