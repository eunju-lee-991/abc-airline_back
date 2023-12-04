package com.abcairline.abc.repository;

import com.abcairline.abc.domain.Reservation;
import com.abcairline.abc.domain.User;
import com.abcairline.abc.domain.UserCoupon;
import com.abcairline.abc.dto.user.UserInfoDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public List<UserCoupon> findUserCouponsForUser(Long userId) {
        return em.createQuery(
                        "SELECT uc FROM UserCoupon uc WHERE uc.user.id = :userId", UserCoupon.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public UserCoupon findUserOneCoupon(Long userCouponId) {
        {
            return em.find(UserCoupon.class, userCouponId);
        }

    }

    public User findOneWithProviderAndProviderId(String provider, String providerId) {
        Optional<User> optionalUser = Optional.ofNullable(DataAccessUtils.uniqueResult(em.createQuery(
                        "SELECT u FROM User u WHERE u.provider = :provider AND u.providerId = :providerId", User.class)
                .setParameter("provider", provider)
                .setParameter("providerId", providerId)
                .getResultList()));

        return optionalUser.isPresent() ? optionalUser.get() : null;
    }
}
