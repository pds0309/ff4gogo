package com.pds.web.api;


import com.pds.openapi.api.UserApiDataCollection;
import com.pds.openapi.api.WhoseMatchDetail;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MatchFinder {

    private final UserApiDataCollection userApi;

    @Autowired
    @Qualifier("usermatch")
    private WhoseMatchDetail<List<JSONObject>> userMatchDetail;

    public List<JSONObject> getDetailMatchJsonListFromCodeList(String matchCode,String userId){
        Map<String , List<String>> matchMap = new HashMap<>();
        matchMap.put(userId,userApi.getUserMatchesDetailFromApi(Collections.singletonList(matchCode)));
        return userMatchDetail.fromJSONToDetailMatch((HashMap<String, List<String>>) matchMap);
    }
}
