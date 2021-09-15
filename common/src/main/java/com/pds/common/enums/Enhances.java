package com.pds.common.enums;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public enum Enhances {

    E0(0,0),
    E1(1,3),
    E2(2,4),
    E3(3,5),
    E4(4,7),
    E5(5,9),
    E6(6,11),
    E7(7,14),
    E8(8,18),
    E9(9,22),
    E10(10,27);
    private final Integer enhanceCode;
    private final Integer enhanceOverall;

    private Enhances(Integer code , Integer overall){
        this.enhanceCode = code;
        this.enhanceOverall = overall;
    }
    private static Map<Integer, Integer> enhanceMap;

    public static Integer getOverall(Integer i){
        if(enhanceMap == null){
            initMapping();
        }
        return enhanceMap.get(i);
    }
    private static void initMapping(){
        log.info("강화 ENUM");
        enhanceMap = new HashMap<>();
        for(Enhances e : values()){
            enhanceMap.put(e.enhanceCode, e.enhanceOverall);
        }
    }
    public static Map<Integer , Integer> getEnhanceMap(){
        if(enhanceMap == null){
            initMapping();
        }
        return enhanceMap;
    }
}
