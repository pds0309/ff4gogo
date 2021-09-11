package com.pds.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatId implements Serializable {
    @OneToOne(targetEntity = Player.class)
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @Column(name = "MATCH_SID")
    private int matchSid;
}
