package com.pds.schedule.api;


import com.pds.common.api.WhoseMatchDetail;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("rankermatch")
@Log4j2
public class RankerMatchDetailImpl implements WhoseMatchDetail<List<JSONObject>> {
    @Override
    public List<JSONObject> fromJSONToDetailMatch(HashMap<String, List<String>> matchList) {
        List<JSONObject> jsonResultList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : matchList.entrySet()) {
            try {
                log.info("FOUND USER - [" + entry.getKey() + "] : " + entry.getValue().size() + " MATCH");
                for (int i = 0; i < entry.getValue().size(); i++) {
                    JSONObject jsonObject = new JSONObject(entry.getValue().get(i)).getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("matchInfo");

                    int j = (jsonArray.getJSONObject(0).getString("accessId").equals(entry.getKey()) ? 0 : 1);
                    int matchSeasonId = jsonArray.getJSONObject(j).getJSONObject("matchDetail").getInt("seasonId");
                    String matchResult = jsonArray.getJSONObject(j).getJSONObject("matchDetail").getString("matchResult");
                    jsonArray = (JSONArray) jsonArray.getJSONObject(j).get("player");

                    for (int k = 0; k < jsonArray.length(); k++) {
                        int pos = jsonArray.getJSONObject(k).getInt("spPosition");
                        if (pos == 28) {
                            continue;
                        }
                        int pId = jsonArray.getJSONObject(k).getInt("spId");
                        jsonObject = jsonArray.getJSONObject(k).getJSONObject("status");
                        jsonResultList.add(new JSONObject().put("goal", jsonObject.getInt("goal"))
                                .put("assist", (jsonObject.getInt("assist")))
                                .put("star", jsonObject.getInt("spRating"))
                                .put("cnt", 1)
                                .put("win", (matchResult.equals("승") ? 1 : 0))
                                .put("pId", pId)
                                .put("statId", matchSeasonId));
                    }
                }
            } catch (JSONException ignored) {
                log.info("[WRONG DATA]");
            }
        }
        return jsonResultList;
    }
}
