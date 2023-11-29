package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.Flight;
import com.abcairline.abc.domain.Seat;
import com.abcairline.abc.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FlightRepository {
    private final EntityManager em;

    public void save(Flight flight) {
        em.persist(flight);
    }

    public Flight findOne(Long id) {
        return em.find(Flight.class, id);
    }

    public List<Flight> findAll() {
        return em.createQuery("select f from Flight f", Flight.class)
                .getResultList();
    }

    public List<Flight> findFlightsByRoute(String departureCode, String arrivalCode, LocalDateTime searchDate) {
        return em.createQuery("select f from Flight f where f.route.departure.id = :departureCode " +
                        "and f.route.arrival.id = :arrivalCode", Flight.class)
                .setParameter("departureCode", departureCode)
                .setParameter("arrivalCode", arrivalCode)
                // 나중에 날짜 추가
                .getResultList();
    }

    public List<Seat> findAllSeats(Long flightId)  {
        return em.createQuery("SELECT s FROM Seat s WHERE s.flight.id = :flightId", Seat.class)
                .setParameter("flightId", flightId)
                .getResultList();
    }

    public Seat findSeat(Long seatId)  {
        return em.find(Seat.class, seatId);
    }
}
