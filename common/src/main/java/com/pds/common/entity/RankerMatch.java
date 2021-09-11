package com.pds.common.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@AllArgsConstructor
public class RankerMatch {
    @EmbeddedId
    private final RankerMatchId rankerMatchId;
    @Column
    private final int matchHit;
}
