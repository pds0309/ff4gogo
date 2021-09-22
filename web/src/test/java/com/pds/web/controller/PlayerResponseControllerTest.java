package com.pds.web.controller;


import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import com.pds.web.service.PlayerResponseService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PlayerInfoResponseController.class)
class PlayerResponseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlayerResponseService responseService;

    @Test
    void getPlayerInfoTest() throws Exception {
        int pid = 101000001;
        PlayerDto.Info info =
                new PlayerDto.Info(101000001,"David Simon",new SeasonDto(101,"name","info","img"));
        PlayerDto.InfoWithImage infoWithImage = new PlayerDto.InfoWithImage(info,"pimgUrl.png");
        given(responseService.getPlayer(pid)).willReturn(infoWithImage);
        MvcResult mvcResult = mvc.perform(get("/players").param("pid",pid+""))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject result = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(infoWithImage.getInfo().getPlayerName(),result.getJSONObject("data").getJSONObject("info").getString("playerName"));
        assertEquals(infoWithImage.getPImg() , result.getJSONObject("data").getString("pimg"));
    }
}
