package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class FlightRoute {

    @Id
    @GeneratedValue
    @Column(name = "route_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "departure_code")
    private Airport departure;
    @ManyToOne
    @JoinColumn(name = "arrival_code")
    private Airport arrival;
}
