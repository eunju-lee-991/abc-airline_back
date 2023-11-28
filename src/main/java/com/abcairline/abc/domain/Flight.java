package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Flight {

    @Id @GeneratedValue
    @Column(name = "flight_id")
    private Long id;

    private String flightNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "route_id")
    private FlightRoute route;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private int price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "airplane_model")
    private Airplane airplane;

    @OneToMany(mappedBy = "flight")
    private List<Seat> seats;
}
