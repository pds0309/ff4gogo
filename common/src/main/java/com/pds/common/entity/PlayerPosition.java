package com.pds.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayerPosition {
    @Id
    private int playerId;

    //FW
    @Column(nullable = false)
    private int lw;
    @Column(nullable = false)
    private int cf;
    @Column(nullable = false)
    private int rw;
    @Column(nullable = false)
    private int st;
    //MF
    @Column(nullable = false)
    private int cdm;
    @Column(nullable = false)
    private int lm;
    @Column(nullable = false)
    private int cm;
    @Column(nullable = false)
    private int rm;
    @Column(nullable = false)
    private int cam;
    //DF
    @Column(nullable = false)
    private int sw;
    @Column(nullable = false)
    private int lwb;
    @Column(nullable = false)
    private int lb;
    @Column(nullable = false)
    private int cb;
    @Column(nullable = false)
    private int rb;
    @Column(nullable = false)
    private int rwb;
    //GK
    @Column(nullable = false)
    private int gk;
}
