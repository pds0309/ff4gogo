package com.pds.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pds.web.api.MatchDto;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import com.pds.web.service.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserDetailController.class)
class UserDetailControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailService detailService;


    private final List<MatchDto.MatchPlayerDto> matchPlayerDtoList = new ArrayList<>();
    @BeforeEach
    void setMvc() throws JsonProcessingException {
        String match1 = "{\"spRating\":0,\"dribbleTry\":0,\"ballPossesionTry\":0,\"passSuccess\":0,\"defending\":0,\"blockTry\":0,\"effectiveShoot\":0,\"redCards\":0,\"aerialSuccess\":0,\"spGrade\":2,\"aerialTry\":0,\"tackleTry\":0,\"block\":0,\"shoot\":0,\"passTry\":0,\"goal\":0,\"dribbleSuccess\":0,\"spId\":300195859,\"intercept\":0,\"assist\":0,\"dribble\":0,\"tackle\":0,\"ballPossesionSuccess\":0,\"yellowCards\":0,\"spPosition\":28}\n";
        String match2 = "{\"spRating\":6.6,\"dribbleTry\":7,\"ballPossesionTry\":0,\"passSuccess\":6,\"defending\":0,\"blockTry\":0,\"effectiveShoot\":0,\"redCards\":0,\"aerialSuccess\":0,\"spGrade\":8,\"aerialTry\":0,\"tackleTry\":2,\"shoot\":0,\"block\":0,\"passTry\":8,\"goal\":1,\"dribbleSuccess\":7,\"spId\":300245279,\"intercept\":1,\"assist\":1,\"dribble\":101,\"tackle\":1,\"ballPossesionSuccess\":0,\"yellowCards\":0,\"spPosition\":7}\n";
        String match3 = "{\"spRating\":3.4,\"dribbleTry\":7,\"ballPossesionTry\":0,\"passSuccess\":6,\"defending\":0,\"blockTry\":0,\"effectiveShoot\":0,\"redCards\":0,\"aerialSuccess\":0,\"spGrade\":8,\"aerialTry\":0,\"tackleTry\":2,\"shoot\":0,\"block\":0,\"passTry\":8,\"goal\":2,\"dribbleSuccess\":7,\"spId\":300245279,\"intercept\":1,\"assist\":2,\"dribble\":101,\"tackle\":1,\"ballPossesionSuccess\":0,\"yellowCards\":0,\"spPosition\":7}\n";
        ObjectMapper objectMapper = new ObjectMapper();
        matchPlayerDtoList.add(objectMapper.readValue(match1,MatchDto.MatchPlayerDto.class));
        matchPlayerDtoList.add(objectMapper.readValue(match2,MatchDto.MatchPlayerDto.class));
        matchPlayerDtoList.add(objectMapper.readValue(match3,MatchDto.MatchPlayerDto.class));
    }

    @Test
    void myMvpPlayerTest() throws Exception {
        List<MatchDto.BestDto> bestDtoList = new ArrayList<>();
        bestDtoList.add(new MatchDto.BestDto(101123,5,6.5,3,1));
        String body = toJSON(matchPlayerDtoList);
        given(detailService.getMyMvpList(matchPlayerDtoList)).willReturn(bestDtoList);
        mvc.perform(post("/users/{id}/matches/mvp","ID")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .content(body))
                .andExpect(status().isOk()).andExpect(content().contentType("application/json"));
    }

    @Test
    void myMvpPlayerNoDataTest() throws Exception{
        List<MatchDto.MatchPlayerDto> emptyList = new ArrayList<>();
        given(detailService.getMyMvpList(emptyList)).willThrow(
                new UserRequestException(ErrorInfo.FF4_API_ERROR)
        );
        mvc.perform(post("/users/{id}/matches/mvp","ID")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJSON(emptyList)))
                .andExpect(status().isBadRequest());
    }

    private String toJSON(List<MatchDto.MatchPlayerDto> matchPlayerDtoList) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(matchPlayerDtoList);
    }
}