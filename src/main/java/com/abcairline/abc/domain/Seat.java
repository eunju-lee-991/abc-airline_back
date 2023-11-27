package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seat {

    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
    private String seatNumber;
    private boolean isAvailable;
}
