package com.pds.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pds.common.dto.UsersDto;
import com.pds.web.exception.ErrorInfo;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class ResponseHandlerTest {


    @Test
    void responseHandlerTest(){
        String objString = "hello";
        ResponseEntity<String> responseEntity = ResponseHandler.generateResponse("msg", HttpStatus.OK,objString);
        assertEquals(200 , responseEntity.getStatusCodeValue());
        JSONObject result = new JSONObject(responseEntity);
        assertEquals("hello" , result.getJSONObject("body").getString("data"));
        assertEquals("msg",result.getJSONObject("body").getString("message"));
    }

    @Test
    void responseHandlerTest2() throws JsonProcessingException {
        ErrorInfo errorInfo = ErrorInfo.USER_DATA_NOT_EXIST;
        UsersDto.Info userDto = new UsersDto.Info("name이름","ID1",250 , 2000);
        ResponseEntity<UsersDto.Info> responseEntity =
                ResponseHandler
                        .generateResponse(
                                errorInfo.getErrorMsg(),errorInfo.getErrorCode(),HttpStatus.BAD_REQUEST,userDto,"/users");
        assertEquals(400 , responseEntity.getStatusCodeValue());
        JSONObject result = new JSONObject(responseEntity);
        assertEquals(errorInfo.getErrorCode(),result.getJSONObject("body").getInt("code"));
        assertEquals(errorInfo.getErrorMsg(),result.getJSONObject("body").getString("message"));
        assertEquals("/users",result.getJSONObject("body").getString("path"));

        ObjectMapper objectMapper = new ObjectMapper();


        UsersDto.Info returnDto = objectMapper.readValue(result.getJSONObject("body").getJSONObject("data").toString() , UsersDto.Info.class);
        assertEquals(userDto.getUserId(),returnDto.getUserId());
        assertEquals(userDto.getUserName(),returnDto.getUserName());
    }
}
