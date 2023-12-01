package com.abcairline.abc.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Coupon {

    @Id
    @Column(name = "coupon_id")
    @GeneratedValue
    private Long id;
    private String couponName;
    private int discountPrice;
    private int validDays;
}
