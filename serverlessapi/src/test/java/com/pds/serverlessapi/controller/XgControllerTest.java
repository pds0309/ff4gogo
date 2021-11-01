package com.pds.serverlessapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pds.serverlessapi.api.XgApi;
import com.pds.serverlessapi.api.XgDto;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.NestedServletException;

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
    void getDetailFromMatchCodeTest() throws Exception {
        given(xgApi.getXgVal("SOMETHING"))
                .willReturn(xgDto);
        MvcResult mvcResult = mvc.perform(post("/expectedgoal")
                .content("SOMETHING")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0.25,new JSONObject(mvcResult.getResponse().getContentAsString()).getDouble("prediction"));
    }
}
