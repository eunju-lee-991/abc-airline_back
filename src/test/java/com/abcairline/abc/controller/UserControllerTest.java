package com.abcairline.abc.controller;

import com.abcairline.abc.dto.user.UserInfoDto;
import com.abcairline.abc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    void test() {
        UserInfoDto oneUser = userController.findOneUser(100L);
        System.out.println(oneUser);
    }
}