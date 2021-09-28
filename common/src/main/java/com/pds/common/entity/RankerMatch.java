package com.pds.common.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankerMatch {
    @EmbeddedId
    private RankerMatchId rankerMatchId;
    @Column
    private int matchHit;
}
