package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
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
    private LocalDateTime signUpDate;
    private LocalDateTime lastAccessDate;
    private String role;
    @OneToMany(mappedBy = "user")
    List<Reservation> reservations;
}
