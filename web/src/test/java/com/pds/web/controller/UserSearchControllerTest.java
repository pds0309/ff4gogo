package com.pds.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pds.common.dto.UsersDto;
import com.pds.common.enums.Ranks;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import com.pds.web.service.UserSearchService;
import com.pds.web.utils.ApiExceptionHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserSearchController.class)
class UserSearchControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserSearchService userSearchService;

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }

    @BeforeEach
    void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(new UserSearchController(userSearchService))
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    private final UsersDto.Info infoDto = new UsersDto.Info("NAME", "ID", 200, 2000);

    @Test
    void saveUserTest() throws Exception {
        given(userSearchService.createUser(userSearchService.findUser("NAME")))
                .willReturn(infoDto);
        MockHttpServletResponse response = mvc.perform(post("/users").content("NAME")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andReturn().getResponse();

        JSONObject resultObj = new JSONObject(response.getContentAsString());
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject dtoJson = new JSONObject(objectMapper.writeValueAsString(infoDto));
        assertEquals(dtoJson.getString("userName"), resultObj.getJSONObject("data").getString("userName"));

    }

    @Test
    void saveExceptionInvalidNickNameTest() throws Exception {
        given(userSearchService.createUser(userSearchService.findUser("NAME")))
                .willThrow(new UserRequestException("닉네임을 2글자 이상 입력해주세요", ErrorInfo.PARAMETER_INVALID.getErrorCode()));

        MvcResult mvcResult =
                mvc.perform(post("/users").content("NAME")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(result -> assertEquals(getApiResultExceptionClass(result), UserRequestException.class))
                        .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());

        JSONObject result = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(-103, result.getInt("code"));
        assertEquals("닉네임을 2글자 이상 입력해주세요", result.getString("message"));
    }

    @Test
    void saveExceptionNotFoundUserFromApiTest() throws Exception {
        given(userSearchService.createUser(userSearchService.findUser("NAME")))
                .willThrow(new UserRequestException(ErrorInfo.USER_DATA_NOT_EXIST));

        MvcResult mvcResult =
                mvc.perform(post("/users").content("NAME")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(result -> assertEquals(getApiResultExceptionClass(result), UserRequestException.class))
                        .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
        JSONObject result = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(ErrorInfo.USER_DATA_NOT_EXIST.getErrorCode(), result.getInt("code"));
    }

    @Test
    void getUserTest() throws Exception {
        given(userSearchService.getUserInfo("ID")).willReturn(infoDto);
        MvcResult mvcResult =
                mvc.perform(get("/users/{id}", "ID")
                ).andExpect(status().isOk())
                        .andExpect(model().attribute("user", infoDto))
                        .andExpect(model().attribute("rank", Ranks.getRanks(infoDto.getHighRank()).getRankInfo()))
                        .andReturn();
        assertEquals(infoDto, mvcResult.getModelAndView().getModel().get("user"));
    }

    @Test
    void getUserExceptionTest() throws Exception {
        given(userSearchService.getUserInfo("ID")).willThrow(
                new UserRequestException(ErrorInfo.USER_BAD_ACCESS));
        MvcResult mvcResult =
                mvc.perform(get("/users/{id}", "ID"))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeDoesNotExist("user"))
                        .andExpect(model().attribute("errorcode", 400))
                        .andExpect(model().attribute("errorname", "BAD_REQUEST"))
                        .andReturn();

        assertEquals("error", mvcResult.getModelAndView().getViewName());
    }

    @Test
    void saveUserMatchesTest() throws Exception {
        List<String> matchCodeList = new ArrayList<>();
        matchCodeList.add("A");
        matchCodeList.add("B");
        given(userSearchService.getMatchListFirstTime("ID"))
                .willReturn(matchCodeList);
        mvc.perform(post("/users/{id}/matches", "ID"))
                .andExpect(status().isCreated())
                .andReturn();
    }
    @Test
    void saveUserMatchesNotExistTest() throws Exception {
        given(userSearchService.getMatchListFirstTime("ID"))
                .willThrow(
                        new UserRequestException("유저의 최근 개인순위경기 기록이 없습니다!", ErrorInfo.USER_DATA_NOT_EXIST.getErrorCode()));
        mvc.perform(post("/users/{id}/matches", "ID"))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals(getApiResultExceptionClass(result), UserRequestException.class));
    }
    @Test
    void response404Test() throws Exception {
        //404
        mvc.perform(get("/fawfwailfhawli"))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void responseNotAllowedMethodTest() throws Exception {
        MvcResult mvcResult =
                //post 방식의 것을 get으로 요청 -> 405 에러를 200 에러페이지로
                mvc.perform(get("/users"))
                        .andExpect(status().isOk())
                        .andExpect(result -> assertEquals(getApiResultExceptionClass(result), HttpRequestMethodNotSupportedException.class))
                        .andReturn();
        assertEquals("error", mvcResult.getModelAndView().getViewName());
        assertEquals(405, mvcResult.getModelAndView().getModel().get("errorcode"));
    }

}
