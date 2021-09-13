package com.pds.common.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
// web , schedule 모두에서 사용될 클래스
public class UserApiDataCollection {

    private final Fifa4SearchUserMatchApi matchApi;
    private final Fifa4SearchUserApi userApi;

    // 유저 Id , 조회할 경기 수로 경기 코드 목록을 조회하여 리턴합니다.
    public List<String> getUserMatchesFromApi(String userId, int matchNum) {
        String jsonResult = matchApi.getUserMatch(userId, matchNum);
        if (jsonResult != null && !jsonResult.equals("{data: []}")) {
            return matchApi.fromJSONtoMatchList(jsonResult);
        } else {
            return new ArrayList<>();
        }
    }

    // 매치코드 리스트를 매치 세부정보 리스트로 리턴합니다.
    public List<String> getUserMatchesDetailFromApi(List<String> matchCode){
        return matchCode.stream().map(matchApi::getUserMatchDetail).collect(Collectors.toList());
    }

    public String getUserIdFromNickName(String name){
        return userApi.getUserAccessId(userApi.getUserInfo(name));
    }
}
