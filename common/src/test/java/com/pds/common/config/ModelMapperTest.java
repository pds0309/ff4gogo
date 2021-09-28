package com.pds.common.config;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import com.pds.common.entity.Player;
import com.pds.common.entity.Season;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ModelMapperConfig.class , GenModelMapper.class})
class ModelMapperTest {

    @Autowired
    private ModelMapper modelMapper;

    private final SeasonDto seasonDto = new SeasonDto(101,"101시즌","101시즌입니다.","101url");

    @Test
    void playerDtoToPlayer(){
        PlayerDto.Info playerDto = new PlayerDto.Info(101123,"김갑환",seasonDto);
        Player player = modelMapper.map(playerDto,Player.class);
        assertEquals(player.getPlayerId(),player.getPlayerId());
        assertEquals(player.getSeason().getInfo(),player.getSeason().getInfo());
    }

    @Autowired
    private GenModelMapper genModelMapper;

    @Test
    void genModelMapperDtoToEntityTest(){
        PlayerDto.Info playerDto = new PlayerDto.Info(101123,"김갑환",seasonDto);
        Player player = (Player) genModelMapper.dtoToEntity(playerDto,Player.class);
        assertEquals(player.getPlayerId(),playerDto.getPlayerId());
        assertEquals(player.getSeason().getInfo(),playerDto.getSeason().getInfo());
        assertEquals(player.getSeason().getId(),playerDto.getSeason().getId());
    }
    @Test
    void genModelMapperEntityToDtoTest(){
        Player player = new Player(101123,"김갑환", Season.builder(seasonDto).build());
        PlayerDto.Info playerDto = (PlayerDto.Info) genModelMapper.entityToDto(player,PlayerDto.Info.class);

        assertEquals(player.getPlayerName(),playerDto.getPlayerName());
        assertEquals(player.getPlayerId(),playerDto.getPlayerId());
        assertEquals(player.getSeason().getImg(),playerDto.getSeason().getImg());
        assertEquals(player.getSeason().getId(),playerDto.getSeason().getId());
    }
}
