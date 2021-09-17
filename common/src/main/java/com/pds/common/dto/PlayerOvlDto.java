package com.pds.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerOvlDto {
    private int playerId;
    private String position;
    private int ovl;
    private int wage;
}
