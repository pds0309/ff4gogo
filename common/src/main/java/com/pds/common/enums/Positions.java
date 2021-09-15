package com.pds.common.enums;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;


@Log4j2
public enum Positions {

    GK(0,"GK","GK"),
    SW(1,"SW","DF"),
    RWB(2,"RWB","DF"),
    RB(3,"RB","DF"),
    RCB(4,"RCB","DF"),
    CB(5,"CB","DF"),
    LCB(6,"LCB","DF"),
    LB(7,"LB","DF"),
    LWB(8,"LWB","DF"),
    RDM(9,"RDM","MF"),
    CDM(10,"CDM","MF"),
    LDM(11,"LDM","MF"),
    RM(12,"RM","MF"),
    RCM(13,"RCM","MF"),
    CM(14,"CM","MF"),
    LCM(15,"LCM","MF"),
    LM(16,"LM","MF"),
    RAM(17,"RAM","MF"),
    CAM(18,"CAM","MF"),
    LAM(19,"LAM","MF"),
    RF(20,"RF","FW"),
    CF(21,"CF","FW"),
    LF(22,"LF","FW"),
    RW(23,"RW","FW"),
    RS(24,"RS","FW"),
    ST(25,"ST","FW"),
    LS(26,"LS","FW"),
    LW(27,"LW","FW"),
    SUB(28,"SUB","SUB");

    @Getter
    private final Integer posCode;
    @Getter
    private final String posInfo;
    @Getter
    private final String rootPosInfo;

    private Positions(Integer code , String info , String root){
        this.posCode = code;
        this.posInfo = info;
        this.rootPosInfo = root;
    }

    private static Map<Integer, Positions> positionMap;

    public static Positions getPos(Integer i){
        if(positionMap == null){
            initMapping();
        }
        return positionMap.get(i);
    }

    private static void initMapping(){
        log.info("포지션 ENUM");
        positionMap = new HashMap<>();
        for(Positions p : values()){
            positionMap.put(p.posCode, p);
        }
    }
}
