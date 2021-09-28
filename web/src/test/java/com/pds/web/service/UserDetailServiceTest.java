package com.pds.web.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pds.web.api.MatchDto;
import com.pds.web.exception.UserRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserDetailService.class})
class UserDetailServiceTest {

    @Autowired
    private UserDetailService detailService;

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
        List<MatchDto.BestDto> bestDtoList = detailService.getMyMvpList(matchPlayerDtoList);
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
            detailService.getMyMvpList(matchPlayerDtoList);
        });
    }

}
