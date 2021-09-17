package com.pds.common.entity;


import com.pds.common.dto.PlayerOvlDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "PlayerOvlBuilder")
public class PlayerOvl {

    @Id
    private int playerId;
    @Column
    private String position;
    @Column
    private int ovl;
    @Column
    private int wage;


    public static PlayerOvlBuilder builder(PlayerOvlDto overallDto){
        return PlayerOvlBuilder()
                .playerId(overallDto.getPlayerId())
                .position(overallDto.getPosition())
                .ovl(overallDto.getOvl())
                .wage(overallDto.getWage());
    }
}
