package com.abcairline.abc.domain;

import com.abcairline.abc.domain.enumeration.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public void updateAncillaryService(AncillaryService ancillaryService) {
        this.ancillaryService = ancillaryService;
    }

    public void updateSeat(Seat seat){
        this.seat.cancelSeat();
        seat.reserveSeat();
        this.seat = seat;
    }
}
