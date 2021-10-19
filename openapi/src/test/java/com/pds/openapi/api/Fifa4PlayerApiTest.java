package com.pds.openapi.api;


import com.pds.openapi.config.Fifa4Api;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Fifa4Api.class , Fifa4PlayerApi.class})
class Fifa4PlayerApiTest {

    @Autowired
    private Fifa4PlayerApi fifa4PlayerApi;

    @Test
    void getSeasonMetatDataTest(){
        String res = fifa4PlayerApi.getSeasonMetaData();
        assertNotNull(res);
        assertNotEquals("{data: []}", res);
    }

}
