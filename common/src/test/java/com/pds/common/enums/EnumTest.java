package com.pds.common.enums;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



class EnumTest {

    @Test
    void getRankInfoTest(){
        String myRankInfo = Ranks.getRanks(-1).getRankInfo();
        assertEquals("언랭",myRankInfo);
        assertEquals(Ranks.WC1,Ranks.getRanks(2000));
    }

    @Test
    void getEnhanceMapTest(){
        Map<Integer , Integer> enMap = Enhances.getEnhanceMap();
        assertNotNull(enMap);
        assertEquals(11,enMap.size());
    }

    @Test
    void getEnhanceTest(){
        assertEquals(22 , Enhances.getOverall(9));
    }

    @Test
    void getPositionTest(){
        String pos = Positions.getPos(28).getPosInfo();
        String rootPos = Positions.getPos(26).getRootPosInfo();
        assertEquals("SUB",pos);
        assertEquals("FW",rootPos);
        assertEquals(26,Positions.getPos(26).getPosCode());
        assertEquals(Positions.valueOf("SUB"),Positions.getPos(28));
    }

}
