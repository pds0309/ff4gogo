package com.pds.common.entity;


import com.pds.common.repository.PlayerPositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PlayerPositionTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlayerPositionRepository playerPositionRepository;

    @Test
    void savePositionTest(){
        PlayerPosition playerPosition =
                new PlayerPosition(
                        101123,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5
                );
        testEntityManager.persistAndFlush(playerPosition);
        PlayerPosition found = playerPositionRepository.findById(101123).orElse(null);

        assertEquals(playerPosition,found);
    }
}
