package com.study.jpa.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.jpa.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
