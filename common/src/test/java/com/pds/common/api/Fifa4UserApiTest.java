package com.pds.common.api;


import com.pds.common.config.Fifa4Api;
import com.pds.common.dto.UsersDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Fifa4Api.class, Fifa4SearchUserApi.class, Fifa4SearchUserMatchApi.class})
class Fifa4UserApiTest {


    @Autowired
    private Fifa4SearchUserApi userApi;

    @Autowired
    private Fifa4SearchUserMatchApi matchApi;

    private final String userId = "5ff691ff0e8ce08e2874e0d3";

    @Test
    void getUserTest() {
        String userName = "Youtube윤보미";
        String res = userApi.getUserInfo(userName);
        assertNotNull(res);
        JSONObject jsonObject = new JSONObject(res);
        assertEquals(userName, jsonObject.getString("nickname"));
        assertEquals(userId, jsonObject.getString("accessId"));
    }

    @Test
    void fromJSONtoUserTest() {
        String result = "{\"accessId\":\"5ff691ff0e8ce08e2874e0d3\",\"nickname\":\"Youtube윤보미\",\"level\":376}";
        UsersDto.UserApiResponse userDto = userApi.fromJSONtoUser(result);
        assertEquals("Youtube윤보미",userDto.getUserName());
        assertEquals(userId,userDto.getUserId());
        assertEquals(376,userDto.getLevel());
    }

    @Test
    void errorJsonUserTest(){
        String result = "gaeighalga";
        assertNull(userApi.fromJSONtoUser(result));
        assertNull(userApi.fromJSONtoUser(null));
    }

    @Test
    void getUserNotExistTest() {
        assertNull(userApi.getUserInfo("졵줴호ㅑ쥐왆뉸유젿윕ㄴ듀ㅣ다"));
    }

    @Test
    void getUserMatchTest() {
        // 내 친구가 너무 오래 게임을 안하면 실패할 수도 있다.
        String res = matchApi.getUserMatch(userId, 2);
        assertNotNull(res);
        assertNotEquals("{data: []}", res);

        assertNotNull(matchApi.fromJSONtoMatchList(res));
    }
    @Test
    void getJsonMatchNotExistTest(){
        String result = "{data: []}";
        assertTrue(matchApi.fromJSONtoMatchList(result).isEmpty());
    }

    @Test
    void getUserMatchNotExistTest() {
        String noMatchUserId = "fda01bf2f33bc25c1d5b6b12";
        String res = matchApi.getUserMatch(noMatchUserId, 1);
        assertNotNull(res);
        assertEquals("{data: []}", res);
        //[]
    }

    @Test
    void getUserMatchDetailTest() {
        String matchId = "6139d4a6b4c3eaad958aee1e";
        assertNotNull(matchApi.getUserMatchDetail(matchId));
    }

    @Test
    void getInvalidMatchIdDetailTest() {
        String matchId = "KKKKKKKK";
        assertNull(matchApi.getUserMatchDetail(matchId));
    }

    //유저 랭크 정보
    @Test
    void getRankFromUserIdTest() {
        String result = userApi.getRankFromUserId(userId);
        assertNotNull(result);
        assertNotEquals("{data: []}", result);

        assertEquals(2000, userApi.fromJSONtoUserRank(result));
    }

    @Test
    void getRankNoExistUserIdTest(){
        assertEquals("{data: []}",userApi.getRankFromUserId("r3qithalgengq93"));
    }

    @Test
    void getUnRankUserTest() {
        String result = "{data: []}";
        assertEquals(-1, userApi.fromJSONtoUserRank(result));
    }

    @Test
    void getRankNullTest(){
        assertEquals(-1 , userApi.fromJSONtoUserRank(userApi.getRankFromUserId(null)));
    }

}
