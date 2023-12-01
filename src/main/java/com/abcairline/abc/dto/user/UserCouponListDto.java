package com.abcairline.abc.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserCouponListDto {
    private int count;
    private List<UserCouponDto> data;
}
