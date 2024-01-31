package com.study.jpa.team.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDTO {

    private int teamCode;

    private int parentTeamCode;

    private String teamName;

}
