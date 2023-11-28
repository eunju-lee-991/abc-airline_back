package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Flight;
import com.abcairline.abc.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        return em.createQuery("select f from Flight f", Flight.class).getResultList();
    }

}
