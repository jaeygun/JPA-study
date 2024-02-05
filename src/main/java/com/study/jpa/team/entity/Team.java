package com.study.jpa.team.entity;

import com.study.jpa.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {

    @Id
    private int teamCode;

    private int parentTeamCode;

    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<User> userList;

}
