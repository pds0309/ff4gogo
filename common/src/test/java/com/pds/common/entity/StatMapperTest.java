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
@SpringBootTest(classes = {ModelMapperConfig.class,GenModelMapper.class})
class StatMapperTest {

    @Autowired
    private GenModelMapper modelMapper;

    private final PlayerDto.Info playerDto = new PlayerDto.Info(101123,"김갑환"
            ,new SeasonDto(101,"101시즌","101시즌임","url"));

    @Test
    void StatModelMapperTest(){
        StatDto.StatIdDto statIdDto = new StatDto.StatIdDto(playerDto,202109);
        StatId statId = (StatId) modelMapper.dtoToEntity(statIdDto,StatId.class);

        assertEquals(statId.getMatchSid(),statIdDto.getMatchSid());

        StatDto.StatBodyDto statBodyDto = new StatDto.StatBodyDto(1,2,3,4,5);
        Stat stat = new Stat(statId,statBodyDto);

        StatDto.Info statDtoInfo = (StatDto.Info) modelMapper.entityToDto(stat,StatDto.Info.class);

        assertTrue(statId.getPlayer().getPlayerId()==statIdDto.getPlayer().getPlayerId()
        && stat.getStatId().getPlayer().getPlayerId() == statIdDto.getPlayer().getPlayerId()
        && statDtoInfo.getStatId().getPlayer().getPlayerId() == statId.getPlayer().getPlayerId());

        assertTrue(statId.getPlayer().getSeason().getId()==statIdDto.getPlayer().getSeason().getId()
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
    void statUpdateBuilderTest(){
        Stat prevStat = new Stat(new StatId(new Player(101123,"김갑환",null),202109)
                ,6,10,15,2,1);

        Stat updateStat = new Stat(new StatId(new Player(101123,"김갑환",null),202109)
                ,3,5,3,1,0);

        Stat newStat = Stat.builder(prevStat,updateStat,prevStat.getCnt()+1).build();

        //2 경기 평점 15 + 1경기 평점 3 의 평균 평점은 11 이다.
        assertEquals(11,newStat.getStar());
        //2 경기 평균 6득점 + 1경기 3득점의 평균 득점은 5 이다.
        assertEquals(5, newStat.getGoal());
        assertEquals(3, newStat.getCnt());
        assertEquals(prevStat.getStatId() , newStat.getStatId());
    }
    @Test
    void statDtoBuilderTest(){
        JSONObject jsonObject = new JSONObject().put("goal",1).put("assist",1).put("star",6.5).put("cnt",5).put("win",1);
        StatDto.StatBodyDto bodyDto = StatDto.StatBodyDto.builder(jsonObject).build();
        assertEquals(1,bodyDto.getGoal());
        assertEquals(1,bodyDto.getAssist());
        assertEquals(6.5,bodyDto.getStar());
        assertEquals(5,bodyDto.getCnt());
    }
}
