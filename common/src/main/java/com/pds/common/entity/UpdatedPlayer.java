package com.pds.common.entity;


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
@Builder(builderMethodName = "UpdatedPlayerBuilder")
public class UpdatedPlayer {
    @Id
    private int playerId;
    @Column(nullable = false)
    private String playerName;

    public static UpdatedPlayerBuilder builder(Player player){
        return UpdatedPlayerBuilder()
                .playerId(player.getPlayerId())
                .playerName(player.getPlayerName());
    }
}
