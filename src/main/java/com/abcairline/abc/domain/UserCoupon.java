package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class UserCoupon {

    @Id
    @Column(name = "user_coupon_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean usedYn = false;

    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;

    public void useCoupon() {
        this.usedYn = true;
    }
}
