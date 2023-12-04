package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String name;
    private String password;
    private String imageUrl;
    private boolean socialLoginYn;
    private String provider;
    private String providerId;
    private LocalDateTime signUpDate;
    private LocalDateTime lastAccessDate;
    private String role;
    @OneToMany(mappedBy = "user")
    List<Reservation> reservations;

    @Builder
    public User(String email, String name, String imageUrl, boolean socialLoginYn
            , String provider, String providerId, LocalDateTime signUpDate, LocalDateTime lastAccessDate, String role) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.socialLoginYn = socialLoginYn;
        this.provider = provider;
        this.providerId = providerId;
        this.signUpDate = signUpDate;
        this.lastAccessDate = lastAccessDate;
        this.role = role;
    }
}
