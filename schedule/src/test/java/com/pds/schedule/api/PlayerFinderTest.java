package com.pds.schedule.api;


import com.pds.common.api.Fifa4PlayerApi;
import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PlayerFinder.class})
public class PlayerFinderTest {

    @Autowired
    private PlayerFinder playerFinder;

    @MockBean
    private Fifa4PlayerApi playerApi;


    @Test
    void getPlayerTest(){

        List<PlayerDto.PlayerApiResponse> playerDto = new ArrayList<>();
        playerDto.add(new PlayerDto.PlayerApiResponse(new JSONObject().put("id","101345").put("name","굴리트")));
        playerDto.add(new PlayerDto.PlayerApiResponse(new JSONObject().put("id","101346").put("name","김갑환")));
        given(playerApi.fromJSONtoPlayer(playerApi.getPlayerMetaData())).willReturn(playerDto);
        List<PlayerDto.PlayerApiResponse> found = playerFinder.getRecentPlayerDto();
        assertEquals(playerDto,found);
        assertEquals(playerDto.get(0).getPlayerName() , found.get(0).getPlayerName());
    }
    @Test
    void getSeasonTest(){
        List<SeasonDto> seasonDto = new ArrayList<>();
        seasonDto.add(new SeasonDto(new JSONObject("{\n" +
                "                \"seasonId\": 101,\n" +
                "                \"className\": \"ICON (ICON)\",\n" +
                "                \"seasonImg\": \"https://ssl.nexon.com/s2/game/fo4/obt/externalAssets/season/icon.png\"\n" +
                "        }")));

        given(playerApi.fromJSONtoSeason(playerApi.getSeasonMetaData())).willReturn(seasonDto);
        assertEquals(seasonDto,playerFinder.getRecentSeason());
    }
}
