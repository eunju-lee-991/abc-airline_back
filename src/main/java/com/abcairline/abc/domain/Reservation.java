package com.abcairline.abc.domain;

import com.abcairline.abc.domain.enumeration.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public static Reservation createReservation(User user, Flight flight, AncillaryService ancillaryService, int reservationPrice
            , Seat seat, ReservationStatus status) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setAncillaryService(ancillaryService);
        reservation.setReservationPrice(reservationPrice);
        seat.reserveSeat();
        reservation.setSeat(seat);
        reservation.setStatus(status);

        return reservation;
    }

    public void updateAncillaryService(AncillaryService ancillaryService) {
        this.ancillaryService = ancillaryService;
    }

    public void updateSeat(Seat seat){
        this.seat.cancelSeat();
        seat.reserveSeat();
        this.seat = seat;
    }
}
