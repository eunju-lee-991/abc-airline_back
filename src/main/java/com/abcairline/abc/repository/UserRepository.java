package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.User;
import com.abcairline.abc.dto.user.UserInfoDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    public User findOneWithReservation(Long userId) {
        return em.createQuery(
                        "SELECT u FROM User u WHERE u.id = :userId", User.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
