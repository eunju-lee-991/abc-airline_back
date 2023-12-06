package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Payment;
import com.abcairline.abc.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PayRepository {
    private final EntityManager em;

    public void save(Payment payment) {
        em.persist(payment);
    }

    public Payment findOne(Long paymentId) {
        Optional<Payment> optionalPayment = Optional.ofNullable(DataAccessUtils.uniqueResult(
                em.createQuery("select p from Payment p" +
                        " left join fetch p.userCoupon uc" +
                        " where p.id = :paymentId", Payment.class)
                .setParameter("paymentId", paymentId)
                .getResultList()));

        return optionalPayment.isPresent() ? optionalPayment.get() : null;
    }
}
