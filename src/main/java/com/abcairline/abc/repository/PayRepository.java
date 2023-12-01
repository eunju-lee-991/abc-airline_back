package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Payment;
import com.abcairline.abc.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PayRepository {
    private final EntityManager em;

    public void save(Payment payment) {
        em.persist(payment);
    }

    public Payment findOne(Long paymentId) {
        return em.createQuery("select p from Payment p" +
                        " left join fetch p.userCoupon uc" +
                        " where p.id = :paymentId" , Payment.class)
                .setParameter("paymentId", paymentId)
                .getSingleResult();
    }}
