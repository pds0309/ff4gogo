package com.pds.common.api;


import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class Fifa4PlayerApi {
    private final RestTemplate restTemplate;
    private final HttpEntity<String> requestEntity;

    //No Test - 너무 오래걸림
    public String getPlayerMetaData(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://static.api.nexon.co.kr/fifaonline4/latest/spid.json", HttpMethod.GET, requestEntity, String.class);
        String response = responseEntity.getBody();
        return "{data: " + response + "}";
    }

    public String getSeasonMetaData(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://static.api.nexon.co.kr/fifaonline4/latest/seasonid.json", HttpMethod.GET, requestEntity, String.class);
        String response = responseEntity.getBody();
        return "{data: " + response + "}";
    }

    public List<PlayerDto.PlayerApiResponse> fromJSONtoPlayer(String result) {
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

    public List<SeasonDto> fromJSONtoSeason(String result){
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
