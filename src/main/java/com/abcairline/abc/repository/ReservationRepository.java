package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.User;
import com.abcairline.abc.domain.enumeration.ReservationStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final EntityManager em;

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    public Reservation findOne(Long reservationId) {
        return em.find(Reservation.class, reservationId);
    }

    public Reservation findOneWithAllInformation(Long reservationId) {
        Optional<Reservation> optionalReservation
                = Optional.ofNullable( DataAccessUtils.uniqueResult(
                        em.createQuery(
                        "select r from Reservation r" +
                                " join fetch r.user u" +
                                " join fetch r.flight f" +
                                " join fetch f.route fr" +
                                " join fetch fr.departure da" +
                                " join fetch fr.arrival aa" +
                                " join fetch f.airplane ap" +
                                " join fetch r.seat s" +
                                " WHERE r.id = :reservationId"
                        , Reservation.class)
                .setParameter("reservationId", reservationId)
                .getResultList()));

        return optionalReservation.isPresent() ? optionalReservation.get() : null;
    }
    public List<Reservation> findAll() {
        return em.createQuery("select r from Reservation r " +
                " join fetch r.seat s", Reservation.class).getResultList();
    }

    public List<Reservation> findAllForUser(Long userId) {
        return em.createQuery(
                        "SELECT r FROM Reservation r WHERE r.user.id = :userId", Reservation.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public Integer countAllReservationsForUser(Long userId, ReservationStatus status) {
        return em.createQuery("SELECT count(r) FROM Reservation r WHERE r.user.id = :userId " +
                        " AND r.status = :status", Integer.class)
                .setParameter("userId", userId)
                .setParameter("status", status)
                .getSingleResult();
    }
}
