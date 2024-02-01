package com.study.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.jpa.team.repository.TeamRepository;
import com.study.jpa.user.entity.User;
import com.study.jpa.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class JpaApplicationTests {

    @Autowired UserRepository userRepository;

    @Autowired TeamRepository teamRepository;

    @Test
    @Transactional
    void FetchTypeTest() {

        /* 
         * [ 지연로딩(FetchType.LAZY) 실행 결과 ]
         * Hibernate: select u1_0.userUid,u1_0.teamCode,u1_0.userId,u1_0.userName,u1_0.userPw from user u1_0 where u1_0.userUid=?
         * [사용자 조회] Name : 홍길동, Id : test
         * Hibernate: select t1_0.teamCode,t1_0.parentTeamCode,t1_0.teamName from team t1_0 where t1_0.teamCode=?
         * [사용자 팀 조회] teamname : 테스트팀
         * 
         * [ 즉시로딩(FetchType.EAGER) 실행 결과 ]
         * Hibernate: select u1_0.userUid,u1_0.teamCode,u1_0.userId,u1_0.userName,u1_0.userPw from user u1_0 where u1_0.userUid=?
         * Hibernate: select t1_0.teamCode,t1_0.parentTeamCode,t1_0.teamName from team t1_0 where t1_0.teamCode=?
         * [사용자 조회] Name : 홍길동, Id : test
         * [사용자 팀 조회] teamname : 테스트팀
         */
        User findUser = userRepository.findUserByUserUid((long) 10);
        System.out.println("[사용자 조회] Name : " + findUser.getUserName() + ", Id : " + findUser.getUserId());
        System.out.println("[사용자 팀 조회] teamname : " + findUser.getTeam().getTeamName());
    }
}
