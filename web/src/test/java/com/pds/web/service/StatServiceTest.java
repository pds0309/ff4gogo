package com.pds.web.service;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.StatDto;
import com.pds.common.entity.Player;
import com.pds.common.entity.Season;
import com.pds.common.entity.Stat;
import com.pds.common.entity.StatId;
import com.pds.common.repository.StatRepository;
import com.pds.common.repository.StatStatIdMatchSidOnly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {StatService.class, ModelMapperConfig.class, GenModelMapper.class})
class StatServiceTest {

    @Autowired
    private StatService statService;

    @MockBean
    private StatRepository statRepository;

    private final List<Stat> statList = new ArrayList<>();

    @BeforeEach
    void setStatList(){
        statList.add(new Stat(
                new StatId(
                        new Player(101123,"김갑환",new Season(101,"","","")),202109),
                5,5,5,15,5,"","ST"));
        statList.add(new Stat(
                new StatId(
                        new Player(201125,"최번개",new Season(201,"","","")),202108),
                2,1,3,15,4,"","ST"));
        statList.add(new Stat(
                new StatId(
                        new Player(301124,"장거한",new Season(301,"","","")),202109),
                2,1,3,2,15,"","ST"));
    }

    @Test
    void cnt75Test(){
        List<Integer> cntList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        given(statRepository.findCnt(202109)).willReturn(cntList);
        assertEquals(9,statService.getCnt75(202109));
    }
    @Test
    void getThisSeasonTest(){
        StatStatIdMatchSidOnly statStatIdMatchSidOnly = new StatStatIdMatchSidOnly() {
            @Override
            public Integer getStatIdMatchSid() {
                return statList.get(0).getStatId().getMatchSid();
            }
        };
        given(statRepository.findTopStatIdMatchSidByOrderByStatIdMatchSidDesc()).willReturn(statStatIdMatchSidOnly);
        assertEquals(statList.get(0).getStatId().getMatchSid() , statService.getThisSeason());
    }

    @Test
    void getThisSeasonMatchNumTest(){
        given(statRepository.findSeasonMatchNum(202109)).willReturn(5);
        assertEquals(5,statService.getThisSeasonMatchNum(202109));
    }

    @Test
    void getTopGoalPlayersTest(){
        given(statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByGoalDesc(1,202109)).willReturn(statList);
        List<StatDto.Info> statDtoList = statService.getTopGoalPlayers(202109,1);
        assertEquals(statDtoList.get(0).getStatId().getPlayer().getPlayerId(),statList.get(0).getStatId().getPlayer().getPlayerId());
        assertEquals(statDtoList.get(1).getStar(),statList.get(1).getStar());
    }
    @Test
    void getTopRatingPlayersTest(){
        given(statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByStarDesc(1,202109)).willReturn(statList);
        List<StatDto.Info> statDtoList = statService.getTopRatingPlayers(202109,1);
        assertEquals(statDtoList.get(0).getStatId().getPlayer().getPlayerId(),statList.get(0).getStatId().getPlayer().getPlayerId());
        assertEquals(statDtoList.get(1).getStar(),statList.get(1).getStar());
    }
    @Test
    void getTopCntPlayersTest(){
        given(statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByCntDesc(1,202109)).willReturn(statList);
        List<StatDto.Info> statDtoList = statService.getTopCntPlayers(202109,1);
        assertEquals(statDtoList.get(0).getStatId().getPlayer().getPlayerId(),statList.get(0).getStatId().getPlayer().getPlayerId());
        assertEquals(statDtoList.get(1).getStar(),statList.get(1).getStar());
    }
    @Test
    void getTopWinPlayersTest(){
        given(statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByWinDesc(1,202109)).willReturn(statList);
        List<StatDto.Info> statDtoList = statService.getTopWinPlayers(202109,1);
        assertEquals(statDtoList.get(0).getStatId().getPlayer().getPlayerId(),statList.get(0).getStatId().getPlayer().getPlayerId());
        assertEquals(statDtoList.get(1).getStar(),statList.get(1).getStar());
    }
}
