package com.pds.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayerPositionDto {
    private int playerId;
    //FW
    private int lw;
    private int cf;
    private int rw;
    private int st;
    //MF
    private int cdm;
    private int lm;
    private int cm;
    private int rm;
    private int cam;
    //DF
    private int sw;
    private int lwb;
    private int lb;
    private int cb;
    private int rb;
    private int rwb;
    //GK
    private int gk;
}
