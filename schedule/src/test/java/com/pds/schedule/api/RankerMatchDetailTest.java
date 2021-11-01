package com.pds.schedule.api;


import com.pds.openapi.api.WhoseMatchDetail;
import com.pds.schedule.TestMatchDetail;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WhoseMatchDetail.class , RankerMatchDetailImpl.class})
class RankerMatchDetailTest {


    @Autowired
    @Qualifier("rankermatch")
    private WhoseMatchDetail<List<JSONObject>> rankerMatchDetail;

    @Test
    void fromJSONToDetailMatchTest(){
        HashMap<String , List<String>> matchMap = new HashMap<>();
        matchMap.put(TestMatchDetail.userId, Collections.singletonList(TestMatchDetail.match));
        List<JSONObject> result = rankerMatchDetail.fromJSONToDetailMatch(matchMap);
        assertNotEquals(0,result.size());
        assertNotNull(result.get(0).get("statId"));
        assertNotNull(result.get(0).get("pId"));
    }
}
