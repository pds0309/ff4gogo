package com.pds.web.api;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.pds.web.TestMatchDetail;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

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

}
