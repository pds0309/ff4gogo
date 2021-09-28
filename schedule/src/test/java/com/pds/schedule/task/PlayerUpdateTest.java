package com.pds.schedule.task;


import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import com.pds.common.entity.Player;
import com.pds.common.entity.Season;
import com.pds.common.entity.UpdatedPlayer;
import com.pds.common.repository.PlayerRepository;
import com.pds.common.repository.SeasonRepository;
import com.pds.common.repository.UpdatedPlayerRepository;
import com.pds.schedule.api.PlayerFinder;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PlayerUpdate.class})
class PlayerUpdateTest {

    @Autowired
    private PlayerUpdate playerUpdate;

    @MockBean
    private PlayerFinder playerFinder;


    @MockBean
    private PlayerRepository playerRepository;
    @MockBean
    private SeasonRepository seasonRepository;
    @MockBean
    private UpdatedPlayerRepository updatedPlayerRepository;

    @Test
    void updateSeasonTest(){
        List<SeasonDto> seasonDtoList = new ArrayList<>();
        seasonDtoList.add(new SeasonDto(new JSONObject("{\n" +
                "                \"seasonId\": 101,\n" +
                "                \"className\": \"ICON (ICON)\",\n" +
                "                \"seasonImg\": \"https://ssl.nexon.com/s2/game/fo4/obt/externalAssets/season/icon.png\"\n" +
                "        }")));
        seasonDtoList.add(new SeasonDto(new JSONObject("{\n" +
                "                \"seasonId\": 202,\n" +
                "                \"className\": \"VTR (VTR)\",\n" +
                "                \"seasonImg\": \"https://ssl.nexon.com/s2/game/fo4/obt/externalAssets/season/vtr.png\"\n" +
                "        }")));

        given(playerFinder.getRecentSeason()).willReturn(seasonDtoList);
        given(seasonRepository.count()).willReturn(2L);
        playerUpdate.updateSeason();
        verify(seasonRepository,times(0)).saveAll(any());

        given(seasonRepository.count()).willReturn(0L);
        playerUpdate.updateSeason();
        verify(seasonRepository,times(1)).saveAll(any());
    }


    @Test
    void updatePlayersTest(){
        List<Player> newPlayerList = new ArrayList<>();
        newPlayerList.add(new Player(10101234,"김갑환",new Season(101,"","","")));
        newPlayerList.add(new Player(10101235,"최번개",new Season(101,"","","")));

        playerUpdate.updatePlayers(newPlayerList);
        verify(playerRepository, times(1)).saveAll(newPlayerList);
        verify(updatedPlayerRepository, times(1)).saveAll(any());

    }
    @Test
    void updatePlayerNodataTest(){
        playerUpdate.updatePlayers(new ArrayList<>());
        verify(playerRepository, times(0)).saveAll(any());
    }

    @Test
    void getNewPlayerListTest() {
        List<PlayerDto.PlayerApiResponse> recentPlayerDto = new ArrayList<>();
        recentPlayerDto.add(new PlayerDto.PlayerApiResponse(new JSONObject().put("id", 101000195).put("name", "김갑환")));
        recentPlayerDto.add(new PlayerDto.PlayerApiResponse(new JSONObject().put("id", 101000196).put("name", "최번개")));
        given(playerFinder.getRecentPlayerDto()).willReturn(recentPlayerDto);
        given(playerRepository.findById(101000195)).willReturn(Optional.empty());

        given(playerRepository.findById(101000196))
                .willReturn(Optional.of(Player.builder(recentPlayerDto.get(1)
                        ,new Season(101,"","","")).build()));
        given(seasonRepository.findById(101)).willReturn(Optional.of(new Season(101,"","","")));
        List<Player> newPlayer = playerUpdate.getNewPlayerList();
        assertEquals(1,newPlayer.size());
        assertEquals("김갑환",newPlayer.get(0).getPlayerName());

        given(playerRepository.count()).willReturn(2L);
        List<Player> noPlayer = playerUpdate.getNewPlayerList();
        assertEquals(0, noPlayer.size());
    }

    @Test
    void countTest() {
        given(playerRepository.count()).willReturn(5L);
        assertEquals(5, playerUpdate.countPlayer());
        given(seasonRepository.count()).willReturn(5L);
        assertEquals(5, playerUpdate.countSeason());
    }

}
