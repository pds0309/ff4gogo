package com.pds.common.api;

import com.pds.common.dto.UsersDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
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

    public UsersDto.UserApiResponse fromJSONtoUser(String result){
        try{
            UsersDto.UserApiResponse userDto = (result!=null)? new UsersDto.UserApiResponse(new JSONObject(result)) : null;
            return userDto;
        }
        catch(JSONException e){
            return null;
        }
    }


    // 유저 아이디로 랭크 정보를 얻기
    public String getRankFromUserId(String userId){
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.nexon.co.kr/fifaonline4/v1.0/users/"+userId+"/maxdivision", HttpMethod.GET, requestEntity, String.class);
        String response = responseEntity.getBody();
        return "{data: " + response + "}";
    }
    public int fromJSONtoUserRank(String result) {
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("data");

        if (jsonArray.length() != 0 && ((JSONObject) jsonArray.get(0)).getInt("matchType") == 50) {
            return ((JSONObject) jsonArray.get(0)).getInt("division");
        } else
        // 개인 순위경기 기록이 없을 때
        // key : -1  value : unrank
        {
            return -1;
        }
    }
}
