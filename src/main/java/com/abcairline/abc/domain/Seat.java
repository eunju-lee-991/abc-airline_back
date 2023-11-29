package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private String seatNumber;

    private boolean isAvailable = true;

    public Seat(Long id) {

    }

    public void reserveSeat() {
        this.isAvailable = false;
    }

    public void cancelSeat() {
        this.isAvailable = true;
    }
}
