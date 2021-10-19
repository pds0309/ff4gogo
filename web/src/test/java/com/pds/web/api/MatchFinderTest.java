package com.pds.web.api;


import com.pds.openapi.api.UserApiDataCollection;
import com.pds.openapi.api.WhoseMatchDetail;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MatchFinder.class})
class MatchFinderTest {


    @Autowired
    private MatchFinder matchFinder;
    @MockBean
    private UserApiDataCollection userApi;
    @MockBean
    @Qualifier("usermatch")
    private WhoseMatchDetail<List<JSONObject>> userMatchDetail;

    @Test
    void getDetailMatchJsonListFromCodeListTest() {
        List<String> matchCodeList = new ArrayList<>();
        matchCodeList.add("MATCHCODE");
        given(userApi.getUserMatchesDetailFromApi(matchCodeList))
                .willReturn(Collections.singletonList("[]"));
        HashMap<String , List<String>> map = new HashMap<>();
        map.put("ID",matchCodeList);
        given(userMatchDetail.fromJSONToDetailMatch(map)).willReturn(null);
        assertNull(matchFinder.getDetailMatchJsonListFromCodeList(matchCodeList.get(0),"ID"));
    }
}
