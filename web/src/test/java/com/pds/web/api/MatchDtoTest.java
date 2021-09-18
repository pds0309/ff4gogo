package com.pds.web.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pds.web.TestMatchDetail;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MatchDtoTest {

    public <D> Object getDto(String jsonResult, Class<D> c) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResult, c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final List<JSONObject> oneMatchList = TestMatchDetail.matchList;
    private static List<MatchDto.BasicDto> basic = new ArrayList<>();
    private static List<MatchDto.SummaryDto> summary = new ArrayList<>();
    private static List<MatchDto.PassDto> pass = new ArrayList<>();
    private static List<List<MatchDto.ShootDto>> shoot = new ArrayList<>();
    private static List<List<MatchDto.MatchPlayerDto>> player = new ArrayList<>();

    @Test
    void basicDtoTest() {
        //한 경기에 대한 나와 상대의 json 리스트이다.
        List<MatchDto.BasicDto> list = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("nickname", jsonObject.getString("nickname"))
                    .put("matchDate", jsonObject.getString("matchDate"))
                    .put("matchResult", jsonObject.getJSONObject("matchDetail").getString("matchResult"))
                    .put("matchEndType", jsonObject.getJSONObject("matchDetail").getInt("matchEndType"))
                    .put("controller", jsonObject.getJSONObject("matchDetail").getString("controller"))
                    .put("goalTotal", jsonObject.getJSONObject("shoot").getInt("goalTotal"));
            MatchDto.BasicDto basicDto = (MatchDto.BasicDto) getDto(jsonResult.toString(), MatchDto.BasicDto.class);
            list.add(basicDto);
        }
        assertEquals("Youtube윤보미", list.get(0).getNickname());
        assertEquals("keyboard", list.get(0).getController());
        assertEquals("2021-09-09T18:42:23", list.get(0).getMatchDate());
        assertEquals("2021-09-09T18:42:23", list.get(1).getMatchDate());
        assertEquals("승", list.get(0).getMatchResult());
        assertEquals("패", list.get(1).getMatchResult());
        assertEquals(0, list.get(0).getMatchEndType());
        assertEquals(1, list.get(0).getGoalTotal());
        basic = list;
    }

    @Test
    void summaryDtoTest() {
        List<MatchDto.SummaryDto> list = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            JSONObject matchDetail = jsonObject.getJSONObject("matchDetail");
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
        assertEquals(53, list.get(0).getPossession());
        assertEquals(1, list.get(0).getOffsideCount());
        assertEquals(3, list.get(0).getShootTotal());
        assertEquals(2, list.get(1).getCornerKick());
        summary = list;
    }

    @Test
    void passDtoTest() {
        List<MatchDto.PassDto> passDtoList = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            MatchDto.PassDto passDto = (MatchDto.PassDto) getDto(jsonObject.getJSONObject("pass").toString(), MatchDto.PassDto.class);
            passDtoList.add(passDto);
        }
        MatchDto.PassDto myResult = passDtoList.get(0);
        assertEquals(115, myResult.getPassTry());
        assertEquals(104, myResult.getPassSuccess());
        assertEquals(98, myResult.getShortPassTry());
        assertEquals(93, myResult.getShortPassSuccess());
        assertEquals(10, myResult.getThroughPassSuccess());
        assertEquals(12, myResult.getThroughPassTry());
        pass = passDtoList;
    }

    @Test
    void matchPlayerDtoTest() {
        List<List<MatchDto.MatchPlayerDto>> matchPlayerDtoList = new ArrayList<>();
        for (JSONObject jsonObject : oneMatchList) {
            JSONArray playerDetail = jsonObject.getJSONArray("player");
            List<MatchDto.MatchPlayerDto> matchPlayerList = new ArrayList<>();
            for (int j = 0; j < playerDetail.length(); j++) {
                JSONObject playerOBJ = playerDetail.getJSONObject(j);
                int spId = playerOBJ.getInt("spId");
                int spPosition = playerOBJ.getInt("spPosition");
                int spGrade = playerOBJ.getInt("spGrade");
                JSONObject status = playerOBJ.getJSONObject("status");
                status.put("spId",spId)
                        .put("spPosition",spPosition)
                        .put("spGrade",spGrade);
                matchPlayerList.add((MatchDto.MatchPlayerDto)getDto(status.toString(), MatchDto.MatchPlayerDto.class));
            }
            matchPlayerDtoList.add(matchPlayerList);
        }
        assertEquals(2,matchPlayerDtoList.size());
        assertEquals(18,matchPlayerDtoList.get(0).size());
        assertEquals(7.8,matchPlayerDtoList.get(0).get(6).getSpRating());
        assertEquals(101001075,matchPlayerDtoList.get(0).get(6).getSpId());
        assertNotEquals(0,matchPlayerDtoList.get(0).get(15).getSpPosition());
        player = matchPlayerDtoList;
    }

    @Test
    void shootDtoTest(){
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
        assertEquals(2,shootDtoList.size());
        MatchDto.ShootDto resultExam = shootDtoList.get(0).get(0);
        assertEquals(2,resultExam.getType());
        assertEquals(2260,resultExam.getGoalTime());
        assertEquals(1,resultExam.getResult());
        assertEquals(101005984,resultExam.getSpId());
        shoot = shootDtoList;
    }

    @AfterAll
    static void test(){
        //요거 하나는 하나의 경기에 대한 나와 상대에 대한 상세 순위경기 정보이다.
        MatchDto.Info matchDto = new MatchDto.Info(
                basic,summary,pass,player,shoot
        );
        assertEquals(basic,matchDto.getBasicDtoList());
        assertEquals(summary,matchDto.getSummaryDtoList());
        assertEquals(pass,matchDto.getPassDtoList());
        assertEquals(player,matchDto.getMatchPlayerDtoList());
        assertEquals(shoot,matchDto.getShootDtoList());
        assertEquals("Youtube윤보미",matchDto.getBasicDtoList().get(0).getNickname());
        assertEquals("전진앞으로",matchDto.getBasicDtoList().get(1).getNickname());
    }

}
