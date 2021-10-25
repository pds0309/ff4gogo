package com.pds.common.entity;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import com.pds.common.dto.StatDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ModelMapperConfig.class, GenModelMapper.class})
class StatMapperTest {

    @Autowired
    private GenModelMapper modelMapper;

    private final PlayerDto.Info playerDto = new PlayerDto.Info(101123, "김갑환"
            , new SeasonDto(101, "101시즌", "101시즌임", "url"));

    @Test
    void StatModelMapperTest() {
        StatDto.StatIdDto statIdDto = new StatDto.StatIdDto(playerDto, 202109);
        StatId statId = (StatId) modelMapper.dtoToEntity(statIdDto, StatId.class);

        assertEquals(statId.getMatchSid(), statIdDto.getMatchSid());

        StatDto.StatBodyDto statBodyDto = new StatDto.StatBodyDto(1, 2, 3, 4, 5, "CM");
        Stat stat = new Stat(statId, statBodyDto);

        StatDto.Info statDtoInfo = (StatDto.Info) modelMapper.entityToDto(stat, StatDto.Info.class);

        assertTrue(statId.getPlayer().getPlayerId() == statIdDto.getPlayer().getPlayerId()
                && stat.getStatId().getPlayer().getPlayerId() == statIdDto.getPlayer().getPlayerId()
                && statDtoInfo.getStatId().getPlayer().getPlayerId() == statId.getPlayer().getPlayerId());

        assertTrue(statId.getPlayer().getSeason().getId() == statIdDto.getPlayer().getSeason().getId()
                && stat.getStatId().getPlayer().getSeason().getId() == statIdDto.getPlayer().getSeason().getId()
                && statDtoInfo.getStatId().getPlayer().getSeason().getId() == statId.getPlayer().getSeason().getId());

        assertTrue(statBodyDto.getCnt() == stat.getCnt()
                && stat.getCnt() == statDtoInfo.getCnt());
        assertTrue(statBodyDto.getStar() == stat.getStar()
                && stat.getStar() == statDtoInfo.getStar());
        assertTrue(statBodyDto.getGoal() == stat.getGoal()
                && stat.getGoal() == statDtoInfo.getGoal());
        assertTrue(statBodyDto.getWin() == stat.getWin()
                && stat.getWin() == statDtoInfo.getWin());
    }


    @Test
    void statUpdateBuilderTest() {
        Stat prevStat = new Stat(new StatId(new Player(101123, "김갑환", null), 202109)
                , 6, 10, 15, 2, 1, "{}", "ST");

        Stat updateStat = new Stat(new StatId(new Player(101123, "김갑환", null), 202109)
                , 3, 5, 3, 1, 0, "{}", "ST");

        Stat newStat = Stat.builder(prevStat, updateStat, prevStat.getCnt() + 1).build();

        //2 경기 평점 15 + 1경기 평점 3 의 평균 평점은 11 이다.
        assertEquals(11, newStat.getStar());
        //2 경기 평균 6득점 + 1경기 3득점의 평균 득점은 5 이다.
        assertEquals(5, newStat.getGoal());
        assertEquals(3, newStat.getCnt());
        assertEquals(prevStat.getStatId(), newStat.getStatId());
    }

    @Test
    void statDtoBuilderTest() {
        JSONObject jsonObject = new JSONObject().put("goal", 1).put("assist", 1).put("star", 6.5).put("cnt", 5).put("win", 1).put("spPosition", 14);
        StatDto.StatBodyDto bodyDto = StatDto.StatBodyDto.builder(jsonObject).build();
        assertEquals(1, bodyDto.getGoal());
        assertEquals(1, bodyDto.getAssist());
        assertEquals(6.5, bodyDto.getStar());
        assertEquals(5, bodyDto.getCnt());
        assertEquals("CM", bodyDto.getSpPosition());
    }

    private final String prevPos = "{\"LM\":1,\"ST\":0,\"CF\":1,\"GK\":0,\"SW\":0,\"RW\":0,\"CM\":0,\"LW\":0,\"CDM\":0,\"CAM\":0,\"RB\":0,\"LB\":0,\"LWB\":0,\"RM\":0,\"RWB\":0,\"CB\":0}";
    private final Stat prevStat = new Stat(new StatId(new Player(101123, "김갑환", null), 202109)
            , 6, 10, 15, 2, 1, prevPos, null);
    @Test
    void setMostPosTest() {
        String currPos = "{\"LM\":1,\"ST\":0,\"CF\":2,\"GK\":0,\"SW\":0,\"RW\":0,\"CM\":0,\"LW\":0,\"CDM\":0,\"CAM\":0,\"RB\":0,\"LB\":0,\"LWB\":0,\"RM\":0,\"RWB\":0,\"CB\":0}";
        prevStat.setMostPos();
        assertEquals("LM", prevStat.getMostPos());
    }

    @Test
    void setMostPosExceptionTest() {
        Stat stat = new Stat(new StatId(new Player(101123, "김갑환", null), 202109)
                , 6, 10, 15, 2, 1, "{}", null);
        stat.setMostPos();
        assertEquals("ST",stat.getMostPos());
    }
    @Test
    void setPosTest(){
        Stat stat = prevStat;
        String realPosition = "LM";
        String result = stat.setPos(realPosition , stat.getSpPosition());
        assertEquals(2 , new JSONObject(result).getInt("LM"));
    }
}
