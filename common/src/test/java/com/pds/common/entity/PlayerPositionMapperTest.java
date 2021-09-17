package com.pds.common.entity;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.PlayerPositionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ModelMapperConfig.class, GenModelMapper.class})
class PlayerPositionMapperTest {
    @Autowired
    private GenModelMapper modelMapper;

    private final PlayerPosition playerPosition =
            new PlayerPosition(
                    101123,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5
            );

    private final PlayerPositionDto playerPositionDto =
            new PlayerPositionDto(
                    101124,6,6,6,7,6,6,6,6,6,6,6,6,6,6,6,6
            );

    @Test
    void dtoToEntityTest(){
        PlayerPosition pos = (PlayerPosition) modelMapper.dtoToEntity(playerPositionDto,PlayerPosition.class);
        assertEquals(playerPositionDto.getPlayerId(),pos.getPlayerId());
        assertEquals(playerPositionDto.getCam(),pos.getCam());
        assertEquals(playerPositionDto.getCdm(),pos.getCdm());
    }

    @Test
    void entityToDtoTest(){
        PlayerPositionDto dto = (PlayerPositionDto) modelMapper.entityToDto(playerPosition,PlayerPositionDto.class);
        assertEquals(playerPosition.getPlayerId(),dto.getPlayerId());
        assertEquals(playerPosition.getCb(),dto.getCb());
        assertEquals(playerPosition.getSt(),dto.getSt());
    }
}
