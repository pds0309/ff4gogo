package com.pds.common.entity;


import com.pds.common.repository.UpdatedPlayerRepository;
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
class UpdatedPlayerTest {
    @Autowired
    private TestEntityManager testEntityManager;


    @Autowired
    private UpdatedPlayerRepository updatedPlayerRepository;

    @Test
    void createPlayerNoArgExceptionTest(){
        UpdatedPlayer player = new UpdatedPlayer();
        assertThrows(PersistenceException.class,()->testEntityManager.persistFlushFind(player));
    }

    @Test
    void createUpdatedPlayerTest(){
        Player pLayer = new Player(101123,"김갑환",new Season(101,"","",""));
        UpdatedPlayer updatedPlayer = UpdatedPlayer.builder(pLayer).build();
        testEntityManager.persistAndFlush(updatedPlayer);
        UpdatedPlayer found = updatedPlayerRepository.findById(101123).orElse(null);
        assertNotNull(found);
        assertEquals(101123,updatedPlayerRepository.findUpdatedPlayerId().get(0));
        assertEquals(101123 , found.getPlayerId());
        assertEquals("김갑환" , found.getPlayerName());

    }


}
