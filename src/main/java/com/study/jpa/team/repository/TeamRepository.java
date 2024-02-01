package com.study.jpa.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.jpa.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer>{

}
