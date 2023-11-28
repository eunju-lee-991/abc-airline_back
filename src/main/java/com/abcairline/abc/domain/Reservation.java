package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.cors.reactive.PreFlightRequestWebFilter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Embedded
    private AncillaryService ancillaryService;

    private int reservationPrice;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public static Reservation createReservation(User user, Flight flight, AncillaryService ancillaryService, int reservationPrice
            , String seatNumber, ReservationStatus status) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setAncillaryService(ancillaryService);
        reservation.setReservationPrice(reservationPrice);
        reservation.setSeatNumber(seatNumber);
        reservation.setStatus(status);

        return reservation;
    }
}
