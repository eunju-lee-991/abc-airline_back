package com.abcairline.abc.service;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.domain.UserCoupon;
import com.abcairline.abc.exception.NotExistUserException;
import com.abcairline.abc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User retrieveOneUser(Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new NotExistUserException();
        }
        return user;
    }

    public User retrieveUserWithProviderAndProviderId(String provider, String providerId) {
        return userRepository.findOneWithProviderAndProviderId(provider, providerId);
    }

    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public List<UserCoupon> retrieveUserCoupons(Long userId) {
        User user = retrieveOneUser(userId);

        if (user == null) {
            throw new NotExistUserException();
        }

        return userRepository.findUserCouponsForUser(userId);
    }

    public UserCoupon retrieveOneUserCoupon(Long userCouponId) {
        UserCoupon userCoupon = userRepository.findUserOneCoupon(userCouponId);

        if (userCoupon == null) {
            System.out.println("Coupon is not exist");
        }

        return userCoupon;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
