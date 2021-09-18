package com.pds.web.service;


import com.pds.web.TestMatchDetail;
import com.pds.web.api.MatchDto;
import com.pds.web.api.MatchFinder;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserMatchService.class})
class UserMatchServiceTest {


    @Autowired
    private UserMatchService matchService;
    @MockBean
    private MatchFinder matchFinder;

    @Test
    void getDetailMatchListTest(){
        List<JSONObject> objects = TestMatchDetail.matchList;
        given(matchFinder.getDetailMatchJsonListFromCodeList("CODE","ID"))
                .willReturn(objects);
        MatchDto.Info result = matchService.getDetailMatchList("CODE","ID");
        assertNotNull(result);
        assertNotNull(result.getMatchPlayerDtoList());
        assertNotNull(result.getBasicDtoList());
        assertNotNull(result.getPassDtoList());
        assertNotNull(result.getSummaryDtoList());
    }

    @Test
    void getDetailMatchEmptyTest(){
        //empty
        JSONObject jsonObject = new JSONObject("{data: []}");
        List<JSONObject> objects = new ArrayList<>();
        objects.add(jsonObject);
        objects.add(TestMatchDetail.matchList.get(1));
        given(matchFinder.getDetailMatchJsonListFromCodeList("CODE","ID"))
                .willReturn(objects);
        Exception e = assertThrows(UserRequestException.class ,
                ()->matchService.getDetailMatchList("CODE","ID"));
        assertEquals(ErrorInfo.FF4_API_ERROR.getErrorMsg() , e.getMessage());

    }
    @Test
    void getDetailMatchNullTest(){
        given(matchFinder.getDetailMatchJsonListFromCodeList("CODE","ID"))
                .willReturn(null);
        Exception e = assertThrows(UserRequestException.class ,
                ()->matchService.getDetailMatchList("CODE","ID"));
        assertEquals(ErrorInfo.FF4_API_ERROR.getErrorMsg() , e.getMessage());
    }
}
