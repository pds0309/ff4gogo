package com.pds.common.entity;


import com.pds.common.repository.StatRepository;
import org.junit.jupiter.api.BeforeEach;
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
class StatTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private StatRepository statRepository;


    @BeforeEach
    void setPlayerAndSeason(){
        Season season = new Season(101,"101시즌","101시즌입니다","url");
        Player player = new Player(101123 ,"김갑환" , season);
        testEntityManager.persistAndFlush(season);
        testEntityManager.persistAndFlush(player);
    }


    @Test
    void createStatNoArgExceptionTest(){
        Stat stat = new Stat();
        assertThrows(PersistenceException.class,()->testEntityManager.persistFlushFind(stat));
    }

    @Test
    void createStatNoStatIdTest(){
        Stat stat = new Stat(null , 1,2,3,4,5);
        assertThrows(PersistenceException.class,()->testEntityManager.persistFlushFind(stat));
    }

    @Test
    void createStatTest(){
        StatId statId =
                new StatId(testEntityManager.find(Player.class,101123),202109);
        Stat stat = new Stat(statId,1,2,3,4,5);
        testEntityManager.persistAndFlush(stat);
        Stat foundStat = statRepository.findById(statId).orElse(null);
        assertEquals(foundStat.getStatId() ,statId);
        assertEquals(foundStat.getStatId().getPlayer().getPlayerId() ,statId.getPlayer().getPlayerId());
        assertEquals(foundStat.getGoal() ,stat.getGoal());
    }

}
