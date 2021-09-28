package com.pds.common.entity;

import com.pds.common.dto.PlayerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "PlayerBuilder")
public class Player{
    @Id
    private int playerId;

    @Column(nullable = false)
    private String playerName;

    @ManyToOne(targetEntity = Season.class)
    @JoinColumn(nullable = false)
    private Season season;

    public static PlayerBuilder builder(PlayerDto.PlayerApiResponse playerDto, Season season){
        return PlayerBuilder()
                .playerId(playerDto.getPlayerId())
                .playerName(playerDto.getPlayerName())
                .season(season);
    }
}
