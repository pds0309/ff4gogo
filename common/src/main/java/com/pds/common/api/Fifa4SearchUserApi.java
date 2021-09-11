package com.pds.common.api;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
public class Fifa4SearchUserApi {


    private final RestTemplate restTemplate;
    private final HttpEntity<String> requestEntity;

    public String getUserInfo(String nickname) {
        try{
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.nexon.co.kr/fifaonline4/v1.0/users?nickname=" + nickname, HttpMethod.GET, requestEntity, String.class);
            return responseEntity.getBody();
        }
        catch(HttpClientErrorException e){
            return null;
        }
    }
    public String getUserAccessId(String reseponse){
        return new JSONObject(reseponse).getString("accessId");
    }
}
