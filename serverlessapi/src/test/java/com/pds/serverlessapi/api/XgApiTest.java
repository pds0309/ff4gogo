package com.pds.serverlessapi.api;


import com.pds.serverlessapi.config.SlApiConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import  static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {XgApi.class , SlApiConfig.class, RestTemplate.class})
class XgApiTest {

    @Autowired
    private XgApi xgApi;

    private final String exampleBody = "{\"assist\": 0, \"asx\": 0 , \"asy\": 0, \"fin\": 1, \"hed\": 0, \"inv\": 0, \"nom\": 0\n" +
            ", \"x\": 0.88 ,\"y\": 0.45}";

    @Test
    void getXgTest(){
        String res = xgApi.getXgVal(exampleBody);
        assertNotNull(res);
        assertTrue(new JSONObject(res).has("prediction"));
    }
    @Test
    void getXgWithInvalidDataTest(){
        String invBody = exampleBody.replace(",\"y\": 0.45","");
        assertThrows(HttpServerErrorException.class, ()->xgApi.getXgVal(invBody));
    }
}
