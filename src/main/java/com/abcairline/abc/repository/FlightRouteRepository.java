package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Airport;
import com.abcairline.abc.domain.Flight;
import com.abcairline.abc.domain.FlightRoute;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FlightRouteRepository {

    private final EntityManager em;

    public FlightRoute findOne(Long id) {
        return em.find(FlightRoute.class, id);
    }

    public List<FlightRoute> findAll() {
        return em.createQuery("select f from FlightRoute f", FlightRoute.class)
                .getResultList();
    }

    public List<Airport> findDepartureByContinent(String continent) {
        return em.createQuery("SELECT DISTINCT fr.departure FROM FlightRoute fr WHERE fr.departure.continent = :continent", Airport.class)
                .setParameter("continent", continent)
                .getResultList();
    }

    public List<Airport> findArrivalByContinent(String continent) {
        return em.createQuery("SELECT DISTINCT fr.arrival FROM FlightRoute fr WHERE fr.arrival.continent = :continent", Airport.class)
                .setParameter("continent", continent)
                .getResultList();
    }


}
