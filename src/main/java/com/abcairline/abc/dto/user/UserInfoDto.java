package com.abcairline.abc.dto.user;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import com.abcairline.abc.dto.reservation.ReservationCountDto;
import com.abcairline.abc.dto.reservation.ReservationDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class UserInfoDto {
    private Long id;
    private String email;
    private String name;
    private String imageUrl;
    private String signUpDate;
    private ReservationCountDto reservationCounts;

    public UserInfoDto(User user, int tempReservationCount) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.imageUrl = user.getImageUrl();
        this.signUpDate = user.getSignUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.reservationCounts = new ReservationCountDto();
        this.reservationCounts.setTempReservationCount(tempReservationCount);
        this.reservationCounts.setConfirmedReservationCount((int) user.getReservations().stream().filter(r -> r.getStatus() == ReservationStatus.CONFIRMED).count());
        this.reservationCounts.setPendingReservationCount((int) user.getReservations().stream().filter(r -> r.getStatus() == ReservationStatus.PENDING).count());
        this.reservationCounts.setCanceledReservationCount((int) user.getReservations().stream().filter(r -> r.getStatus() == ReservationStatus.CANCEL).count());
    }
}
