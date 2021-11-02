package com.pds.serverlessapi.controller;


import com.pds.serverlessapi.api.XgApi;
import com.pds.serverlessapi.api.XgDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = XgController.class)
class XgControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private XgApi xgApi;

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }

    private final XgDto xgDto = new XgDto(0.25);


    @Test
    void expectedGoalForShootTest() throws Exception {
        given(xgApi.getXgVal("Shootinfo"))
                .willReturn(xgDto);
        MvcResult mvcResult = mvc.perform(post("/expectedgoal")
                .content("Shootinfo")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0.25,new JSONObject(mvcResult.getResponse().getContentAsString()).getDouble("prediction"));
    }
    @Test
    void expectedGoalsForShootsTest() throws Exception{
        List<XgDto> xgDtoList = new ArrayList<>();
        xgDtoList.add(xgDto);
        xgDtoList.add(xgDto);
        given(xgApi.getXgListVal("Shootinfo"))
                .willReturn(xgDtoList);
        mvc.perform(post("/expectedgoals")
                .content("Shootinfo")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
