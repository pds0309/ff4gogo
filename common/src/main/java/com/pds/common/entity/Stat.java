package com.pds.common.entity;


import com.pds.common.dto.StatDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "StatUpdateBuilder")
public class Stat {

    @EmbeddedId
    private StatId statId;

    @Column
    private double goal;
    @Column
    private double assist;
    @Column
    private double star;
    @Column
    private int cnt;

    @Column
    private double win;

    public Stat(StatId statId , StatDto.StatBodyDto bodyDto){
        this.statId = statId;
        this.goal = bodyDto.getGoal();
        this.assist = bodyDto.getAssist();
        this.star = bodyDto.getStar();
        this.cnt = bodyDto.getCnt();
        this.win = bodyDto.getWin();
    }

    public static StatBuilder builder(Stat prev , Stat curr , int c) {
        return StatUpdateBuilder()
                .statId(prev.getStatId())
                .goal((prev.getGoal()*(c-1)+curr.getGoal())/c)
                .assist((prev.getAssist()*(c-1)+curr.getAssist())/c)
                .star((prev.getStar()*(c-1)+curr.getStar())/c)
                .cnt(c)
                .win((prev.getWin()*(c-1)+curr.getWin())/c)
                ;
    }
}
