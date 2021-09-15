package com.pds.common.entity;


import com.pds.common.dto.UsersDto;
import com.pds.common.enums.Ranks;
import com.pds.common.repository.UsersRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UsersTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsersRepository usersRepository;


    private final Users users = new Users("Youtube윤보미","ID1",245,2000);
    @Test
    void createUsersNoArgExceptionTest(){
        Users userss = new Users();
        assertThrows(PersistenceException.class,()->entityManager.persistFlushFind(userss));
    }

    @Test
    void createUsersTest(){
        entityManager.persistAndFlush(users);
        Users found = usersRepository.findById("Youtube윤보미").orElse(null);
        assertNotNull(found);
        assertEquals(users,found);
    }
    @Test
    void findIgnoreCaseUserTest(){
        entityManager.persistAndFlush(users);
        Users found = usersRepository.findByUserNameIsIgnoreCase("yOUTUBE윤보미");
        assertNotNull(found);
        assertEquals(users.getUserName(),found.getUserName());
    }
    @Test
    void dtoToEntityTest(){
        UsersDto.Info userDto = new UsersDto.Info("유저","ID",250,2000);
        Users userResult = Users.builder(userDto).build();
        assertEquals(userDto.getUserId(),userResult.getUserId());
        assertEquals(userDto.getHighRank(),userResult.getHighRank());
    }
    @Test
    void apiDtoToInfoDtoTest(){
        UsersDto.UserApiResponse dto = new UsersDto.UserApiResponse(
                new JSONObject().put("nickname","Youtube윤보미")
                        .put("accessId","UID")
                        .put("level", 250)
        );
        UsersDto.Info userDto = new UsersDto.Info(dto,2000);
        assertEquals(dto.getLevel() , userDto.getLevel());
        assertEquals(dto.getUserName(),userDto.getUserName());
    }
}
