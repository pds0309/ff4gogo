package com.pds.common.api;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class Fifa4PlayerImgApiTest {

    @Test
    void test(){
        assertEquals("https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p101000250.png", Fifa4PlayerImgApi.findPlayerImgUrl(101000250));
    }
    @Test
    void noActionImgReturnTest(){
        assertEquals("https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/players/p13743.png",Fifa4PlayerImgApi.findPlayerImgUrl(234013743));
    }

    @Test
    void noImageReturnTest(){
        assertEquals(Fifa4PlayerImgApi.DEFAULT_IMAGE,Fifa4PlayerImgApi.findPlayerImgUrl(111111111));
    }
}
