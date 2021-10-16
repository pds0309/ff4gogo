package com.pds.schedule.api;


import com.pds.openapi.api.Fifa4PlayerApi;
import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerFinder {
    private final Fifa4PlayerApi playerApi;

    public List<PlayerDto.PlayerApiResponse> getRecentPlayerDto() {
        String result = playerApi.getPlayerMetaData();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        List<PlayerDto.PlayerApiResponse> player = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsobj = (JSONObject) jsonArray.get(i);
            PlayerDto.PlayerApiResponse pd = new PlayerDto.PlayerApiResponse(jsobj);
            player.add(pd);
        }
        return player;
    }
    public List<SeasonDto> getRecentSeason(){
        String result = playerApi.getSeasonMetaData();
        List<SeasonDto> seasonDtoList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for(int i = 0; i < jsonArray.length(); i ++){
            jsonObject = (JSONObject) jsonArray.get(i);
            SeasonDto seasonDto = new SeasonDto(jsonObject);
            seasonDtoList.add(seasonDto);
        }
        return seasonDtoList;
    }
}
