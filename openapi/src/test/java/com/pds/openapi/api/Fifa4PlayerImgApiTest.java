package com.pds.openapi.api;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class Fifa4PlayerImgApiTest {

    @Test
    void test(){
        assertEquals("https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p101000250.png", Fifa4PlayerImgApi.findPlayerImgUrl(101000250));
    }
    @Test
    void noImageReturnTest(){
        assertEquals(Fifa4PlayerImgApi.DEFAULT_IMAGE,Fifa4PlayerImgApi.findPlayerImgUrl(111111111));
    }
}
