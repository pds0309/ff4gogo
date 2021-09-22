package com.pds.web.service;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.PlayerDto;
import com.pds.common.entity.Player;
import com.pds.common.entity.Season;
import com.pds.common.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PlayerResponseService.class , ModelMapperConfig.class, GenModelMapper.class})
class PlayerResponseServiceTest {

    @Autowired
    private PlayerResponseService responseService;
    @MockBean
    private PlayerRepository playerRepository;

    @Test
    void getPlayerTest(){
        //데이비드 베컴
        int pId = 101000250;
        Player player = new Player(pId,"데이비드베컴", new Season(101,"name","info","url"));
        given(playerRepository.findById(pId)).willReturn(Optional.of(player));
        PlayerDto.InfoWithImage infoWithImage = responseService.getPlayer(pId);
        assertNotNull(infoWithImage);
        assertEquals(player.getPlayerName() , infoWithImage.getInfo().getPlayerName());
        assertEquals(player.getPlayerId() , infoWithImage.getInfo().getPlayerId());
        assertEquals("https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p101000250.png"
        ,infoWithImage.getPImg());
        assertEquals(player.getSeason().getImg() , infoWithImage.getInfo().getSeason().getImg());
    }

    @Test
    void getPlayerNoDataTest(){
        Player player = new Player(101111111,"알수없음", new Season(101,"name","info","url"));
        given(playerRepository.findById(101111111)).willReturn(Optional.of(player));

        given(playerRepository.findById(99999999)).willReturn(Optional.empty());
        responseService.getPlayer(99999999);
        verify(playerRepository, times(1)).findById(101111111);

    }
}
