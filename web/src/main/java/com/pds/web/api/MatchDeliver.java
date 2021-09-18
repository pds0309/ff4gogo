package com.pds.web.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MatchDeliver {

    private MatchDeliver(){
        //
    }
    public static <D> Object getDto(String jsonResult, Class<D> c) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResult, c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<List<MatchDto.ShootDto>> shootInfo(List<JSONObject> oneMatchList){
        List<List<MatchDto.ShootDto>> shootDtoList = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            JSONArray shootDetail = jsonObject.getJSONArray("shootDetail");
            List<MatchDto.ShootDto> shootList = new ArrayList<>();
            for (int j = 0; j < shootDetail.length(); j++) {
                JSONObject shootOBJ = shootDetail.getJSONObject(j);
                shootList.add((MatchDto.ShootDto)getDto(shootOBJ.toString(), MatchDto.ShootDto.class));
            }
            shootDtoList.add(shootList);
        }
        return shootDtoList;
    }

    public static List<MatchDto.PassDto> passInfo(List<JSONObject> oneMatchList){
        List<MatchDto.PassDto> passDtoList = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            MatchDto.PassDto passDto = (MatchDto.PassDto) getDto(jsonObject.getJSONObject("pass").toString(), MatchDto.PassDto.class);
            passDtoList.add(passDto);
        }
        return passDtoList;
    }

    public static List<List<MatchDto.MatchPlayerDto>> playerInfo(List<JSONObject> oneMatchList){
        List<List<MatchDto.MatchPlayerDto>> matchPlayerDtoList = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            JSONArray playerDetail = jsonObject.getJSONArray("player");
            List<MatchDto.MatchPlayerDto> matchPlayerList = new ArrayList<>();
            for (int j = 0; j < playerDetail.length(); j++) {
                JSONObject playerOBJ = playerDetail.getJSONObject(j);
                JSONObject status = playerOBJ.getJSONObject("status");
                status.put("spId",playerOBJ.getInt("spId"))
                        .put("spPosition",playerOBJ.getInt("spPosition"))
                        .put("spGrade",playerOBJ.getInt("spGrade"));
                matchPlayerList.add((MatchDto.MatchPlayerDto)getDto(status.toString(), MatchDto.MatchPlayerDto.class));
            }
            matchPlayerDtoList.add(matchPlayerList);
        }
        return matchPlayerDtoList;
    }

    private static final String MATCHDETAIL = "matchDetail";

    public static List<MatchDto.BasicDto> basicInfo(List<JSONObject> oneMatchList){
        List<MatchDto.BasicDto> list = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("nickname", jsonObject.getString("nickname"))
                    .put("matchDate", jsonObject.getString("matchDate"))
                    .put("matchResult", jsonObject.getJSONObject(MATCHDETAIL).getString("matchResult"))
                    .put("matchEndType", jsonObject.getJSONObject(MATCHDETAIL).getInt("matchEndType"))
                    .put("controller", jsonObject.getJSONObject(MATCHDETAIL).getString("controller"))
                    .put("goalTotal", jsonObject.getJSONObject("shoot").getInt("goalTotal"));
            MatchDto.BasicDto basicDto = (MatchDto.BasicDto) getDto(jsonResult.toString(), MatchDto.BasicDto.class);
            list.add(basicDto);
        }
        return list;
    }
    public static List<MatchDto.SummaryDto> summaryInfo(List<JSONObject> oneMatchList){
        List<MatchDto.SummaryDto> list = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            JSONObject matchDetail = jsonObject.getJSONObject(MATCHDETAIL);
            JSONObject jsonResult = new JSONObject();
            JSONObject shoot = jsonObject.getJSONObject("shoot");
            jsonResult.put("foul", matchDetail.getInt("foul"))
                    .put("yellowCards", matchDetail.getInt("yellowCards"))
                    .put("redCards", matchDetail.getInt("redCards"))
                    .put("possession", matchDetail.getInt("possession"))
                    .put("offsideCount", matchDetail.getInt("offsideCount"))
                    .put("cornerKick", matchDetail.getInt("cornerKick"))
                    .put("shootTotal", shoot.getInt("shootTotal"))
                    .put("effectiveShootTotal", shoot.getInt("effectiveShootTotal"))
                    .put("ownGoal", shoot.getInt("ownGoal"))
                    .put("shootPenaltyKick", shoot.getInt("shootPenaltyKick"))
                    .put("shootFreekick", shoot.getInt("shootFreekick"));
            MatchDto.SummaryDto summaryDto = (MatchDto.SummaryDto) getDto(jsonResult.toString(), MatchDto.SummaryDto.class);
            list.add(summaryDto);
        }
        return list;
    }

}
