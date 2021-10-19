package com.pds.openapi.api;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
}
