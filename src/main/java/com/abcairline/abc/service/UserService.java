package com.abcairline.abc.service;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User retrieveOneUser(Long id) {
        return userRepository.findOne(id);
    }

    public User retrieveOneUserWithReservation(Long userId) {
        return userRepository.findOneWithReservation(userId);
    }

    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public Long saveUser(User user) {
        return null;
    }

    public Long updateUser(User user) {
        return null;
    }
}
