package com.pds.web.controller;


import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import com.pds.common.dto.StatDto;
import com.pds.common.entity.Player;
import com.pds.common.entity.Season;
import com.pds.common.entity.Stat;
import com.pds.common.entity.StatId;
import com.pds.web.TestMatchDetail;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.service.StatService;
import com.pds.web.utils.ResponseHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatService statService;

    private final List<StatDto.Info> statDtoList = new ArrayList<>();


    @BeforeEach
    void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(new HomeController(statService))
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        statDtoList.add(new StatDto.Info(
                new StatDto.StatIdDto(
                        new PlayerDto.Info(101123, "김갑환", new SeasonDto(101, "", "", "")), 202109),
                5, 5, 5, 15, 5, TestMatchDetail.posForStat, "CF"));
        statDtoList.add(new StatDto.Info(
                new StatDto.StatIdDto(
                        new PlayerDto.Info(101124, "최번개", new SeasonDto(101, "", "", "")), 202108),
                5, 5, 5, 15, 5, TestMatchDetail.posForStat, "ST"));
    }


    @Test
    void homeTest() throws Exception {
        int thisSeason = 202109;
        int cnt = 2;
        given(statService.getThisSeason()).willReturn(thisSeason);
        given(statService.getCnt75(thisSeason)).willReturn(cnt);
        given(statService.getThisSeasonMatchNum(thisSeason)).willReturn(2);

        given(statService.getTopGoalPlayers(thisSeason, cnt)).willReturn(statDtoList);
        given(statService.getTopCntPlayers(thisSeason, cnt)).willReturn(statDtoList);
        given(statService.getTopRatingPlayers(thisSeason, cnt)).willReturn(statDtoList);
        given(statService.getTopWinPlayers(thisSeason, cnt)).willReturn(statDtoList);

        MvcResult mvcResult = mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attributeExists("toprating"))
                .andExpect(model().attribute("standardcnt", 2))
                .andExpect(model().attributeExists("thisseason"))
                .andExpect(model().attribute("topgoal", statDtoList))
                .andExpect(model().attribute("toprating", statDtoList))
                .andReturn();

        List<StatDto.Info> resultList = (ArrayList) mvcResult.getRequest().getAttribute("toprating");
        assertEquals(statDtoList, resultList);

        StatDto.Info result0TopGoal = ((StatDto.Info) ((ArrayList) mvcResult.getRequest().getAttribute("topgoal")).get(0));
        assertEquals(statDtoList.get(0), result0TopGoal);
    }

    @Test
    void getHomeBest11Test() throws Exception {
        int thisSeason = 202109;
        int cnt = 3;
        given(statService.getBest11Players(thisSeason, cnt)).willReturn(statDtoList);
        MvcResult mvcResult = mvc.perform(get("/best11")
                .param("cnt", String.valueOf(cnt))
                .param("season", String.valueOf(thisSeason)))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject res = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(2, res.getJSONArray("data").length());
    }

    @Test
    void getHomeBest11NoDataTest() throws Exception {
        given(statService.getBest11Players(1, 1)).willReturn(new ArrayList<>());
        MvcResult mvcResult = mvc.perform(get("/best11")
                .param("cnt", String.valueOf(1))
                .param("season", String.valueOf(1)))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(ErrorInfo.DB_DATA_ERROR.getErrorCode(),new JSONObject(mvcResult.getResponse().getContentAsString()).getInt("code"));
    }
}
