package com.abcairline.abc.repository;

import com.abcairline.abc.domain.*;
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
        LocalDateTime startDate = searchDate.minusDays(2).toLocalDate().atStartOfDay();
        LocalDateTime endDate = searchDate.plusDays(3).toLocalDate().atStartOfDay().minusSeconds(1);

        return em.createQuery("select f from Flight f where f.route.departure.id = :departureCode " +
                        "and f.route.arrival.id = :arrivalCode AND f.departureDate BETWEEN :startDate AND :endDate order by f.departureDate", Flight.class)
                .setParameter("departureCode", departureCode)
                .setParameter("arrivalCode", arrivalCode)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
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


    public List<Airport> findAllAirports() {
        return em.createQuery("select ap from Airport ap", Airport.class)
                .getResultList();
    }

    public List<Airplane> findAllAirplanes() {
        return em.createQuery("select ap from Airplane ap order by model", Airplane.class)
                .getResultList();
    }
}
