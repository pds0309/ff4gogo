package com.pds.openapi.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("hi")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Fifa4SearchUserMatchApi.class})
class NoApiKeyTest {

    @Autowired
    private Fifa4SearchUserMatchApi userApi;


    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private HttpEntity<String> httpEntity;

    @Test
    void getUserMatchInvalidApiKey(){
        String uId = "5ff691ff0e8ce08e2874e0d3";
        int num = 1;
        given(restTemplate.exchange("https://api.nexon.co.kr/fifaonline4/v1.0/users/" + uId + "/matches?matchtype=50&offset=0&limit="+num, HttpMethod.GET, httpEntity, String.class))
        .willThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        HttpClientErrorException e = assertThrows(HttpClientErrorException.class,() -> userApi.getUserMatch(uId,num));
        assertEquals(403,e.getRawStatusCode());
    }
}
