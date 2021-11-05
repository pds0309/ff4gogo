package com.pds.common.entity;


import com.pds.common.dto.UserCountDto;
import com.pds.common.repository.UserCountRepository;
import org.hibernate.TransientPropertyValueException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserCountTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserCountRepository userCountRepository;

    private final Users users = new Users("김갑환","ID",100,200);

    @BeforeEach
    void before(){
        testEntityManager.persistAndFlush(users);
    }

    @Test
    void saveUserCountNotExistUserTest(){
        UserCount userCount = new UserCount(new Users("A","B",10,10));
        assertThrows(IllegalStateException.class,()->testEntityManager.persistFlushFind(userCount));
    }
    @Test
    void saveUserCountTest(){
        UserCount userCount = new UserCount(new Users("김갑환","ID",100,10));
        testEntityManager.persistAndFlush(userCount);
        UserCount result = userCountRepository.findByUsers(users).orElse(new UserCount());
        assertEquals(users.getUserName(),result.getUsers().getUserName());
        assertEquals(users.getUserId(),result.getUsers().getUserId());
        assertEquals(0,result.getTodayCount());
        assertEquals(0,result.getTotalCount());
        assertNotNull(result.getId());
    }

    @Test
    void updateTodayCountZeroTest(){
        UserCount userCount = new UserCount(users);
        userCount.updateTodayCountCount();
        testEntityManager.persistAndFlush(userCount);
        assertEquals(1,userCount.getTodayCount());
        userCountRepository.updateTodayCountZero();
        UserCount result = userCountRepository.findByUsers(users).orElse(null);
        assertNotNull(result);
        assertEquals(0,result.getTodayCount());
        assertEquals(1,result.getTotalCount());
    }

    @Test
    void entityToDtoTest(){
        UserCount userCount = new UserCount(users);
        UserCountDto userCountDto = UserCountDto.builder(userCount).build();
        assertEquals(userCount.getUsers().getUserName(),userCountDto.getUserName());
        assertEquals(userCount.getTodayCount(),userCountDto.getTodayCount());
        assertEquals(userCount.getTotalCount(),userCountDto.getTotalCount());
    }
}
