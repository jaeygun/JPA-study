package com.study.jpa;

import com.study.jpa.team.entity.Team;
import com.study.jpa.team.repository.TeamRepository;
import com.study.jpa.user.entity.User;
import com.study.jpa.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
class RelationTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void relate1() {
        User user = userRepository.findUserByUserUid(1L);
        System.out.println("UserName : " + user.getUserName());
        System.out.println("UserId : " + user.getUserId());
        System.out.println("UserTeam : " + user.getTeam().getTeamName());
    }

    @Test
    void relate2() {
        Team team = teamRepository.findByTeamCode(1000);
        System.out.println("TeamName : " + team.getTeamName());
        List<User> userList = team.getUserList();
        for (User user : userList) {
            System.out.println("UserName : " + user.getUserName());
        }
    }
}
