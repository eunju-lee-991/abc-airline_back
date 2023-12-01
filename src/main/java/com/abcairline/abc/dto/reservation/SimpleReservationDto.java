package com.abcairline.abc.dto.reservation;

import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.dto.reservation.ancillary.AncillaryServiceStringDto;
import lombok.Data;

@Data
public class SimpleReservationDto {
    private Long id;
    private AncillaryServiceStringDto ancillaryService;
    private String seatNumber;
    private int reservationPrice;
    private ReservationStatus status;

    public SimpleReservationDto(Reservation reservation) {
        this.id = reservation.getId();
        if(reservation.getAncillaryService() != null){
            this.ancillaryService = new AncillaryServiceStringDto(reservation.getAncillaryService());
        }else {
            this.ancillaryService = new AncillaryServiceStringDto();
        }
        this.seatNumber = reservation.getSeat().getSeatNumber();
        this.reservationPrice = reservation.getReservationPrice();
        this.status = reservation.getStatus();
    }
}
