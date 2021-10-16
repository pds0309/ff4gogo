package com.pds.web.api;

import com.pds.openapi.api.WhoseMatchDetail;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Log4j2
@Qualifier("usermatch")
//내꺼 너꺼 분리 작업
public class UserMatchDetailImpl implements WhoseMatchDetail<List<JSONObject>> {
    private static final String MATCHDATE = "matchDate";

    @Override
    public List<JSONObject> fromJSONToDetailMatch(HashMap<String, List<String>> matchMap) {
        String uId = getUserId(matchMap);
        String matchList = matchMap.get(uId).get(0);
        List<JSONObject> matchJsonList = new ArrayList<>();
        JSONObject data = new JSONObject(matchList).getJSONObject("data");
        String date = data.getString(MATCHDATE);
        JSONArray matchInfoList = data.getJSONArray("matchInfo");
        int j = 0;

        if (!matchInfoList.getJSONObject(j).getString("accessId").equals(uId)) {
            j = 1;
            if(!matchInfoList.getJSONObject(j).getString("accessId").equals(uId)){
                throw new UserRequestException(ErrorInfo.USER_BAD_ACCESS);
            }
        }
        matchJsonList.add(matchInfoList.getJSONObject(j).put(MATCHDATE, date));
        matchJsonList.add(matchInfoList.getJSONObject(Math.abs(j - 1)).put(MATCHDATE, date));
        return matchJsonList;
    }

    String getUserId(HashMap<String, List<String>> matchMap) {
        return matchMap.keySet().stream().findFirst().orElseThrow(() ->
                new UserRequestException(ErrorInfo.USER_DATA_NOT_EXIST));
    }
}
