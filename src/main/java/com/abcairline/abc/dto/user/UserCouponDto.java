package com.abcairline.abc.dto.user;

import com.abcairline.abc.domain.UserCoupon;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class UserCouponDto {
    private Long id;
    private String couponName;
    private int discountPrice;
    private boolean usedYn;
    private String creationDate;
    private String expirationDate;

    public UserCouponDto(UserCoupon userCoupon) {
        this.id = userCoupon.getId();
        this.couponName = userCoupon.getCoupon().getCouponName();
        this.discountPrice = userCoupon.getCoupon().getDiscountPrice();
        this.usedYn = userCoupon.isUsedYn();
        this.creationDate = userCoupon.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.expirationDate = userCoupon.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
