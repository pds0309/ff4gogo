package com.pds.openapi.api;


import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


// 유저 아이디로 최근 개인순위경기 아이디를 얻습니다.
@Component
@RequiredArgsConstructor
public class Fifa4SearchUserMatchApi {

    private final RestTemplate restTemplate;
    private final HttpEntity<String> requestEntity;

    public String getUserMatch(String uId , int num) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.nexon.co.kr/fifaonline4/v1.0/users/" + uId + "/matches?matchtype=50&offset=0&limit="+num, HttpMethod.GET, requestEntity, String.class);
            String response = responseEntity.getBody();
            return "{data: " + response + "}";

        } catch (HttpClientErrorException e) {
            return "{data: []}";
        }
    }
    public String getUserMatchDetail(String match) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.nexon.co.kr/fifaonline4/v1.0/matches/" + match, HttpMethod.GET, requestEntity, String.class);
            String response = responseEntity.getBody();
            return "{data: " + response + "}";
        }
        catch(HttpClientErrorException e){
            return null;
        }
    }

    public List<String> fromJSONtoMatchList(String result) {
        List<String> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray ja = jsonObject.getJSONArray("data");
        for (int i = 0; i < ja.length(); i++) {
            list.add(ja.getString(i));
        }
        return list;
    }
}
