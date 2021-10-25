package com.pds.schedule.api;


import com.pds.openapi.api.UserApiDataCollection;
import com.pds.openapi.api.WhoseMatchDetail;
import com.pds.schedule.TestMatchDetail;

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
@SpringBootTest(classes = {RankerMatchFinder.class})
class RankerMatchFinderTest {

    private final List<String> matchList = Collections.singletonList("6139d4a6b4c3eaad958aee1e");

    private final String userId = TestMatchDetail.userId;
    @Autowired
    private RankerMatchFinder rankerMatchFinder;

    @MockBean
    private UserApiDataCollection userApiDataCollection;

    @MockBean
    @Qualifier("rankermatch")
    private WhoseMatchDetail<List<JSONObject>> detail;

    @Test
    void getRankersMatchCodeMapTest() {
        List<String> list = new ArrayList<>();
        list.add(userId);
        given(userApiDataCollection.getUserMatchesFromApi(userId, 10)).willReturn(matchList);
        Map<String,List<String>> result = rankerMatchFinder.getRankersMatchCodeMap(list);
        assertEquals(matchList.get(0), result.get(userId).get(0));
        System.out.println(result);
    }

    @Test
    void getDetailRankerMatchMapTest(){
        Map<String,List<String>> result = new HashMap<>();
        result.put("5ff691ff0e8ce08e2874e0d3", Collections.singletonList("6139d4a6b4c3eaad958aee1e"));
        List<JSONObject> jsonList = new ArrayList<>();
        jsonList.add(new JSONObject(TestMatchDetail.match));
        given(detail.fromJSONToDetailMatch((HashMap<String, List<String>>) result)).willReturn(jsonList);
        assertEquals(jsonList,rankerMatchFinder.getDetailRankerMatchList(result));
    }

}
