package com.abcairline.abc.service;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.domain.UserCoupon;
import com.abcairline.abc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User retrieveOneUser(Long id) {
        return userRepository.findOne(id);
    }

    public User retrieveUserWithProviderAndProviderId(String provider, String providerId) {
        return userRepository.findOneWithProviderAndProviderId(provider, providerId);
    }

    public User retrieveOneUserWithReservation(Long userId) {
        return userRepository.findOneWithReservation(userId);
    }

    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public List<UserCoupon> retrieveUserCoupons(Long userId) {
        return userRepository.findUserCouponsForUser(userId);
    }

    public UserCoupon retrieveOneUserCoupon(Long userCouponId) {
        return userRepository.findUserOneCoupon(userCouponId);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

//    public Long updateUser(User user) {
//        return null;
//    }
}
