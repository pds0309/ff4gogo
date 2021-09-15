package com.pds.common.enums;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;


@Log4j2
public enum Ranks {
    UNRANK(-1, "언랭"),
    SUCHAM(800, "슈퍼챔피언스"),
    CHAM(900, "챔피언스"),
    SUCHAL(1000, "슈퍼챌린지"),
    CHAL1(1100, "챌린지1"),
    CHAL2(1200, "챌린지2"),
    CHAL3(1300, "챌린지3"),
    WC1(2000, "월드클래스1"),
    WC2(2100, "월드클래스2"),
    WC3(2200, "월드클래스3"),
    PRO1(2300, "프로1"),
    PRO2(2400, "프로2"),
    PRO3(2500, "프로3"),
    SEMIPRO1(2600, "세미프로1"),
    SEMIPRO2(2700, "세미프로2"),
    SEMIPRO3(2800, "세미프로3"),
    UMANG1(2900, "유망주1"),
    UMANG2(3000, "유망주2"),
    UMANG3(3100, "유망주3");

    private final Integer rankCode;
    @Getter
    private final String rankInfo;

    private static Map<Integer, Ranks> ranksMap;

    private Ranks(Integer rankCode, String rankInfo){
        this.rankCode = rankCode;
        this.rankInfo = rankInfo;
    }

    public static Ranks getRanks(Integer i){
        if(ranksMap == null){
            initMapping();
        }
        return ranksMap.get(i);
    }

    private static void initMapping(){
        log.info("랭크 ENUM");
        ranksMap = new HashMap<>();
        for(Ranks s : values()){
            ranksMap.put(s.rankCode, s);
        }
    }
}
