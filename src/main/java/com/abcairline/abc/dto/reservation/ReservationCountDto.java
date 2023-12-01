package com.abcairline.abc.dto.reservation;

import lombok.Data;

@Data
public class ReservationCountDto {
    private int tempReservationCount;
    private int confirmedReservationCount;
    private int pendingReservationCount;
    private int canceledReservationCount;
}
