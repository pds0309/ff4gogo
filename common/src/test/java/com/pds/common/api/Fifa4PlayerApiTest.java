package com.pds.common.api;


import com.pds.common.config.Fifa4Api;
import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Fifa4Api.class , Fifa4PlayerApi.class})
class Fifa4PlayerApiTest {

    @Autowired
    private Fifa4PlayerApi fifa4PlayerApi;

    @Test
    void getPlayerMetaDataTest(){
        String res = "{data: [{\"id\": 507261070, \"name\": \"켄자바예프\" }]}";
        List<PlayerDto.PlayerApiResponse> playerApiResponseList =  fifa4PlayerApi.fromJSONtoPlayer(res);
        assertEquals("켄자바예프",playerApiResponseList.get(0).getPlayerName());
        assertEquals(507261070,playerApiResponseList.get(0).getPlayerId());
    }

    @Test
    void getSeasonMetatDataTest(){
        String res = fifa4PlayerApi.getSeasonMetaData();
        assertNotNull(res);
        List<SeasonDto> seasonDtoList = fifa4PlayerApi.fromJSONtoSeason(res);
        assertNotEquals(0,seasonDtoList.size());
    }

}
