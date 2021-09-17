package com.pds.common.entity;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.PlayerOvlDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ModelMapperConfig.class, GenModelMapper.class})
class PlayerOvlMapperTest {

    @Autowired
    private GenModelMapper<PlayerOvlDto> modelMapper;

    @Test
    void dtoToEntityTest() {
        PlayerOvlDto dto = new PlayerOvlDto(101123, "CM", 100, 18);
        PlayerOvl playerOvl = PlayerOvl.builder(dto).build();

        assertEquals(dto.getPlayerId(), playerOvl.getPlayerId());
        assertEquals(dto.getOvl(), playerOvl.getOvl());
        assertEquals(dto.getPosition(), playerOvl.getPosition());
        assertEquals(dto.getWage(), playerOvl.getWage());
    }

    @Test
    void entityToDtoTest() {
        PlayerOvl playerOvl = new PlayerOvl(101123,"CAM",90,15);
        PlayerOvlDto playerOvlDto = modelMapper.entityToDto(playerOvl,PlayerOvlDto.class);
        assertEquals(playerOvl.getOvl(),playerOvlDto.getOvl());
        assertEquals(playerOvl.getPlayerId(),playerOvlDto.getPlayerId());
        assertEquals(playerOvl.getWage(),playerOvlDto.getWage());
        assertEquals(playerOvl.getPosition(),playerOvlDto.getPosition());
    }
}
