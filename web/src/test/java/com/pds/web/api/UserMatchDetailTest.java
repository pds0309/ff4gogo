package com.pds.web.api;


import com.pds.openapi.api.WhoseMatchDetail;
import com.pds.web.TestMatchDetail;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WhoseMatchDetail.class , UserMatchDetailImpl.class})
class UserMatchDetailTest {

    @Autowired
    @Qualifier("usermatch")
    private WhoseMatchDetail<List<JSONObject>> userMatchDetail;

    @Test
    void fromJSONToDetailMatchTest(){
        HashMap<String , List<String>> matchMap = new HashMap<>();
        List<String> matchList = new ArrayList<>();
        matchList.add(TestMatchDetail.match);
        matchList.add(TestMatchDetail.match2);
        matchMap.put(TestMatchDetail.userId, matchList);
        List<JSONObject> result = userMatchDetail.fromJSONToDetailMatch(matchMap);
        assertFalse(result.isEmpty());
        assertEquals(2,result.size());
        assertEquals(TestMatchDetail.userId,result.get(0).getString("accessId"));
    }
    @Test
    void fromJSONToDetailInvalidUserAccessTest(){
        HashMap<String , List<String>> matchMap = new HashMap<>();
        List<String> matchList = new ArrayList<>();
        matchList.add(TestMatchDetail.match);
        matchList.add(TestMatchDetail.match2);
        matchMap.put("INVALIDUSERID", matchList);
        Exception e = assertThrows(UserRequestException.class ,
                ()->userMatchDetail.fromJSONToDetailMatch(matchMap));
        assertEquals(ErrorInfo.USER_BAD_ACCESS.getErrorMsg() , e.getMessage());
    }

    @Test
    void fromJSONToDetailExceptionTest(){
        HashMap<String , List<String>> matchMap = new HashMap<>();
        assertThrows(UserRequestException.class,
                ()->userMatchDetail.fromJSONToDetailMatch(matchMap));
    }

    @Test
    void fromJSONToDetailNoDataTest(){
        HashMap<String , List<String>> matchMap = new HashMap<>();
        List<String> matchList = new ArrayList<>();
        matchList.add(TestMatchDetail.match2);
        matchList.add(TestMatchDetail.matchNodata);
        matchMap.put(TestMatchDetail.userId,matchList);
        List<JSONObject> result = userMatchDetail.fromJSONToDetailMatch(matchMap);
        assertEquals(2,result.size());
    }
}
