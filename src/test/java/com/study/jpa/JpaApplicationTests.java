package com.study.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.jpa.user.entity.User;
import com.study.jpa.user.repository.UserRepository;

@SpringBootTest
class JpaApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void contextLoads() {

        User user = new User();
        user.setUserName("홍길동1");
        user.setUserId("test");
        user.setUserPw("test123");
        userRepository.save(user);
    }

}
