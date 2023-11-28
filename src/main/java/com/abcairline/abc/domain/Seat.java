package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Seat {

    @Id
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private String seatNumber;

    private boolean isAvailable;
}
