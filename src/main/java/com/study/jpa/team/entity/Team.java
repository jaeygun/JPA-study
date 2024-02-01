package com.study.jpa.team.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {

    @Id
    private int teamCode;

    private int parentTeamCode;

    private String teamName;

}
