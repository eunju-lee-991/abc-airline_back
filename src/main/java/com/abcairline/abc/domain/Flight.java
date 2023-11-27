package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Flight {

    @Id @GeneratedValue
    @Column(name = "flight_id")
    private Long id;
    private String flightNumber;
    @ManyToOne
    @JoinColumn(name = "route_id")
    private FlightRoute route;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private int price;
    @ManyToOne
    @JoinColumn(name = "airplane_model")
    private Airplane airplane;

    @OneToMany(mappedBy = "flight")
    private List<Seat> seats;
}
