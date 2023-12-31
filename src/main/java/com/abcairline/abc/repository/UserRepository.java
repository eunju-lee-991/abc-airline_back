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
        Optional<User> optionalUser = Optional.ofNullable(DataAccessUtils.uniqueResult(em.createQuery(
                        "SELECT u FROM User u" +
                                " join fetch u.reservations r" +
                                " WHERE u.id = :userId", User.class)
                .setParameter("userId", id)
                .getResultList()));

        return optionalUser.isPresent() ? optionalUser.get() : null;
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u join fetch u.reservations r", User.class).getResultList();
    }

    public List<UserCoupon> findUserCouponsForUser(Long userId) {
        return em.createQuery(
                        "SELECT uc FROM UserCoupon uc " +
                                " join fetch uc.coupon c" +
                                " WHERE uc.user.id = :userId", UserCoupon.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public UserCoupon findOneUserCoupon(Long userCouponId) {
        Optional<UserCoupon> optionalUserCoupon = Optional.ofNullable(DataAccessUtils.uniqueResult(
                em.createQuery(
                        "SELECT uc FROM UserCoupon uc" +
                                " join fetch uc.coupon c" +
                                " WHERE uc.id = :userCouponId" , UserCoupon.class)
                .setParameter("userCouponId", userCouponId)
                .getResultList()));

        return optionalUserCoupon.isPresent() ? optionalUserCoupon.get() : null;
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
