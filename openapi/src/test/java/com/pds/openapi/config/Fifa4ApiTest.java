package com.pds.openapi.config;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Fifa4Api.class})
class Fifa4ApiTest {

    @Autowired
    private Fifa4Api fifa4Api;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpEntity<String> httpEntity;

    @Test
    void fifa4GetKeyTest() {
        assertNotNull(httpEntity.getHeaders().get("Authorization"));
    }

    @Test
    void fifa4ApiTest() {
        Assertions.assertEquals(fifa4Api.restTemplate(), restTemplate);
    }
}
