package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class FlightRoute {

    @Id
    @GeneratedValue
    @Column(name = "route_id")
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "departure_code")
    private Airport departure;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "arrival_code")
    private Airport arrival;
}
