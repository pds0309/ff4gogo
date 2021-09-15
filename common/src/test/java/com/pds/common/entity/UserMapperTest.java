package com.pds.common.entity;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.UsersDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ModelMapperConfig.class, GenModelMapper.class})
class UserMapperTest {

    @Autowired
    private GenModelMapper<UsersDto.Info> modelMapper;

    private final Users users = new Users("Youtube윤보미","ID1",245,2000);

    @Test
    void entityToDtoTest(){
        UsersDto.Info userDto = modelMapper.entityToDto(users,UsersDto.Info.class);
        assertEquals(users.getUserId(),userDto.getUserId());
        assertEquals(users.getUserName(),userDto.getUserName());
        assertEquals(users.getHighRank(),userDto.getHighRank());
        assertEquals(users.getLevel(),userDto.getLevel());
    }
}
