package com.pds.web.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.pds.web.TestMatchDetail;
import com.pds.web.exception.UserRequestException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchDelieverTest {

    @Test
    void basicInfoTest(){
        List<MatchDto.BasicDto> basicList = MatchDeliver.basicInfo(TestMatchDetail.matchList);
        assertFalse(basicList.isEmpty());
        assertNotNull(basicList.get(0));
        assertNotNull(basicList.get(1));
    }
    @Test
    void summaryInfoTest(){
        List<MatchDto.SummaryDto> summaryDtoList = MatchDeliver.summaryInfo(TestMatchDetail.matchList);
        assertFalse(summaryDtoList.isEmpty());
        assertNotNull(summaryDtoList.get(0));
        assertNotNull(summaryDtoList.get(1));

    }
    @Test
    void passInfoTest(){
        List<MatchDto.PassDto> passList = MatchDeliver.passInfo(TestMatchDetail.matchList);
        assertFalse(passList.isEmpty());
        assertNotNull(passList.get(0));
        assertNotNull(passList.get(1));

    }
    @Test
    void playerInfoTest(){
        List<List<MatchDto.MatchPlayerDto>> playerList = MatchDeliver.playerInfo(TestMatchDetail.matchList);
        assertFalse(playerList.isEmpty());
        assertFalse(playerList.get(0).isEmpty());
        assertFalse(playerList.get(1).isEmpty());
        assertEquals(18,playerList.get(0).size());
        assertEquals(18,playerList.get(1).size());
    }
    @Test
    void shootInfoTest(){
        List<List<MatchDto.ShootDto>> shootList = MatchDeliver.shootInfo(TestMatchDetail.matchList);
        assertFalse(shootList.isEmpty());
        assertFalse(shootList.get(0).isEmpty());
        assertFalse(shootList.get(1).isEmpty());
    }

    @Test
    void getDtoTest(){
        String shootInfo = "{\"type\":2,\"goalTime\":2260,\"spId\":101005984,\"hitPost\":false,\"assistY\":0.65454351902008,\"assistSpId\":300235212,\"result\":1,\"assistX\":0.7920165657997131,\"spGrade\":1,\"x\":0.8487700819969177,\"assist\":true,\"y\":0.4789868891239166,\"inPenalty\":true}\n";
        JSONObject shootJSON = new JSONObject(shootInfo);

        MatchDto.ShootDto shootDto = (MatchDto.ShootDto) MatchDeliver.getDto(shootJSON.toString(),MatchDto.ShootDto.class);
        assertNotNull(shootDto);
        assertTrue(shootDto.isAssist());
        assertTrue(shootDto.isInPenalty());
        assertEquals(shootJSON.getDouble("assistX"),shootDto.getAssistX());
        assertEquals(shootJSON.getDouble("assistY"),shootDto.getAssistY());
    }

    @Test
    void calGoalTimeTest(){
        int time = 16779446;
        assertEquals(82 , MatchDeliver.calGoalTime(time));

        int time2 = 245;
        assertEquals(4 , MatchDeliver.calGoalTime(time2));

        int time3 = 33555462;

        assertEquals(107 , MatchDeliver.calGoalTime(time3));

        int time4 = 50331953;

        assertEquals(109 , MatchDeliver.calGoalTime(time4));
    }


    @Test
    void getMyMvpTest() throws JsonProcessingException {
        String match1 = "{\"spRating\":0,\"dribbleTry\":0,\"ballPossesionTry\":0,\"passSuccess\":0,\"defending\":0,\"blockTry\":0,\"effectiveShoot\":0,\"redCards\":0,\"aerialSuccess\":0,\"spGrade\":2,\"aerialTry\":0,\"tackleTry\":0,\"block\":0,\"shoot\":0,\"passTry\":0,\"goal\":0,\"dribbleSuccess\":0,\"spId\":300195859,\"intercept\":0,\"assist\":0,\"dribble\":0,\"tackle\":0,\"ballPossesionSuccess\":0,\"yellowCards\":0,\"spPosition\":28}\n";
        String match2 = "{\"spRating\":6.6,\"dribbleTry\":7,\"ballPossesionTry\":0,\"passSuccess\":6,\"defending\":0,\"blockTry\":0,\"effectiveShoot\":0,\"redCards\":0,\"aerialSuccess\":0,\"spGrade\":8,\"aerialTry\":0,\"tackleTry\":2,\"shoot\":0,\"block\":0,\"passTry\":8,\"goal\":1,\"dribbleSuccess\":7,\"spId\":300245279,\"intercept\":1,\"assist\":1,\"dribble\":101,\"tackle\":1,\"ballPossesionSuccess\":0,\"yellowCards\":0,\"spPosition\":7}\n";
        String match3 = "{\"spRating\":3.4,\"dribbleTry\":7,\"ballPossesionTry\":0,\"passSuccess\":6,\"defending\":0,\"blockTry\":0,\"effectiveShoot\":0,\"redCards\":0,\"aerialSuccess\":0,\"spGrade\":8,\"aerialTry\":0,\"tackleTry\":2,\"shoot\":0,\"block\":0,\"passTry\":8,\"goal\":2,\"dribbleSuccess\":7,\"spId\":300245279,\"intercept\":1,\"assist\":2,\"dribble\":101,\"tackle\":1,\"ballPossesionSuccess\":0,\"yellowCards\":0,\"spPosition\":7}\n";
        List<MatchDto.MatchPlayerDto> matchPlayerDtoList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        matchPlayerDtoList.add(objectMapper.readValue(match1,MatchDto.MatchPlayerDto.class));
        matchPlayerDtoList.add(objectMapper.readValue(match2,MatchDto.MatchPlayerDto.class));
        matchPlayerDtoList.add(objectMapper.readValue(match3,MatchDto.MatchPlayerDto.class));
        List<MatchDto.BestDto> bestDtoList = MatchDeliver.getMyMvpList(matchPlayerDtoList);
        assertEquals(2,bestDtoList.size());
        assertEquals(3 , bestDtoList.get(1).getAssist());
        assertEquals(3 , bestDtoList.get(1).getGoal());
        assertEquals(5 , bestDtoList.get(1).getSpRating());
        assertEquals(300195859 , bestDtoList.get(0).getSpId());
        assertEquals(2 , bestDtoList.get(1).getCnt());
    }

    @Test
    void getMyMvpNodataTest(){
        List<MatchDto.MatchPlayerDto> matchPlayerDtoList = new ArrayList<>();
        assertThrows(UserRequestException.class , ()->{
            MatchDeliver.getMyMvpList(matchPlayerDtoList);
        });
    }

}
