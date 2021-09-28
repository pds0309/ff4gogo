package com.pds.web.controller;


import com.pds.web.api.MatchDto;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import com.pds.web.service.UserMatchService;
import com.pds.web.utils.ApiExceptionHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserMatchController.class)
class UserMatchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserMatchService matchService;

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }

    @BeforeEach
    void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(new UserMatchController(matchService))
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void getDetailFromMatchCodeTest() throws Exception {
        given(matchService.getDetailMatchList("CODE", "ID"))
                .willReturn(new MatchDto.Info());
        mvc.perform(get("/users/{id}/matches", "ID")
                .param("mc", "CODE"))
                .andExpect(status().isOk()).andReturn();

    }
    @Test
    void getDetailFromMatchCodeExceptionTest() throws Exception{
        given(matchService.getDetailMatchList("CODE","ID"))
                //cover JSONException , NullPointerException
                .willThrow(new UserRequestException(ErrorInfo.FF4_API_ERROR));
        MvcResult result = mvc.perform(get("/users/{id}/matches","ID")
        .param("mc","CODE"))
                .andExpect(status().isBadRequest()).andReturn();
        assertEquals(ErrorInfo.FF4_API_ERROR.getErrorMsg(),result.getResolvedException().getMessage());
    }
    @Test
    void getDetailFromMatchCodeMissingParamExceptionTest() throws Exception {
        given(matchService.getDetailMatchList("CODE","ID"))
                .willReturn(new MatchDto.Info());
        MvcResult mvcResult = mvc.perform(get("/users/{id}/matches","ID")
        ).andExpect(status().isBadRequest())
        .andExpect(result->
                assertEquals(MissingServletRequestParameterException.class,getApiResultExceptionClass(result))).andReturn();
        assertEquals(ErrorInfo.PARAMETER_INVALID.getErrorCode(),
                new JSONObject(mvcResult.getResponse().getContentAsString()).getInt("code"));
    }
}
