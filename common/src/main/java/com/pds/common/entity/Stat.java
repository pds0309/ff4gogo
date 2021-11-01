package com.pds.common.entity;


import com.pds.common.dto.StatDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.AbstractMap;

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

    @Column(columnDefinition = "varchar(255) default '{\"LM\":0,\"ST\":0,\"CF\":0,\"GK\":0,\"SW\":0,\"RW\":0,\"CM\":0,\"LW\":0,\"CDM\":0,\"CAM\":0,\"RB\":0,\"LB\":0,\"LWB\":0,\"RM\":0,\"RWB\":0,\"CB\":0}'")
    @Setter
    private String spPosition;

    @Column(columnDefinition = "varchar(10) default 'NONE'")
    private String mostPos;

    public Stat(StatId statId , StatDto.StatBodyDto bodyDto){
        this.statId = statId;
        this.goal = bodyDto.getGoal();
        this.assist = bodyDto.getAssist();
        this.star = bodyDto.getStar();
        this.cnt = bodyDto.getCnt();
        this.win = bodyDto.getWin();
        this.spPosition = bodyDto.getSpPosition();
    }

    public static StatBuilder builder(Stat prev , Stat curr , int c) {
        return StatUpdateBuilder()
                .statId(prev.getStatId())
                .goal((prev.getGoal()*(c-1)+curr.getGoal())/c)
                .assist((prev.getAssist()*(c-1)+curr.getAssist())/c)
                .star((prev.getStar()*(c-1)+curr.getStar())/c)
                .cnt(c)
                .win((prev.getWin()*(c-1)+curr.getWin())/c)
                .spPosition(prev.setPos(curr.getSpPosition(), prev.getSpPosition()))
                ;
    }
    public String setPos(String realPosition , String prevPos){
        return new JSONObject(prevPos).increment(realPosition).toString();
    }
    public void setMostPos(){
        this.mostPos = new JSONObject(this.spPosition)
                .toMap()
                .entrySet()
                .stream().max((o1, o2) ->
                        Integer.parseInt(o1.getValue().toString())- Integer.parseInt(o2.getValue().toString()))
                .orElseGet(() -> new AbstractMap.SimpleEntry<String,Object>("ST","0")).getKey();
    }
}
