package com.pds.schedule.api;

import com.pds.openapi.api.UserApiDataCollection;
import com.pds.openapi.api.WhoseMatchDetail;
import com.pds.schedule.scrap.RankerScrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Log4j2
public class RankerMatchFinder {

    private final UserApiDataCollection userApiDataCollection;

    @Autowired
    @Qualifier("rankermatch")
    private WhoseMatchDetail<List<JSONObject>> matchDetail;

    public List<String> getRankerIdList() {
        List<String> rankerNameList = RankerScrapper.getRankerListFromWeb(9);
        List<String> rankerIdList = new ArrayList<>();
        for (String s : rankerNameList) {
            JSONObject jsonObject = userApiDataCollection.getUserInfo(s);
            if (jsonObject != null) {
                rankerIdList.add(jsonObject.getString("accessId"));
            }
        }
        return rankerIdList;
    }

    public Map<String, List<String>> getRankersMatchCodeMap(List<String> rankerIdList) {
        Map<String, List<String>> rankerMatchMap = new HashMap<>();
        for (String s : rankerIdList) {
            rankerMatchMap.put(s, userApiDataCollection.getUserMatchesFromApi(s, 20));
        }
        return rankerMatchMap;
    }

    public List<JSONObject> getDetailRankerMatchList(Map<String, List<String>> rankerMatchMap) {
        for (Map.Entry<String, List<String>> entry : rankerMatchMap.entrySet()) {
            entry.setValue(userApiDataCollection.getUserMatchesDetailFromApi(entry.getValue()));
        }
        return matchDetail.fromJSONToDetailMatch((HashMap<String, List<String>>) rankerMatchMap);
    }
}
