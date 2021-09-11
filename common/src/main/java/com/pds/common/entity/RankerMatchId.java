package com.pds.common.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
public class RankerMatchId implements Serializable {
    @Column
    private final String matchCode;
    @Column
    private final String userId;
}
