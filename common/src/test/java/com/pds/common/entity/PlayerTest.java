package com.pds.common.entity;


import com.pds.common.dto.PlayerDto;
import com.pds.common.repository.PlayerRepository;
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
class PlayerTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlayerRepository playerRepository;

    private final Season season = new Season(1,"1","시즌1","1url");

    @Test
    void createPlayerNoArgExceptionTest(){
        Player player2 = new Player();
        assertThrows(PersistenceException.class,()->testEntityManager.persistFlushFind(player2));
    }
    @Test
    void createPlayerNoSeasonExceptionTest(){
        Player player = new Player(101,"김갑환",null);
        assertThrows(PersistenceException.class,()->testEntityManager.persistFlushFind(player));
    }
    @Test
    void createPlayerSeasonTest(){
        testEntityManager.persistAndFlush(season);
        Player player = new Player(101,"김갑환",season);
        Player foundPlayer = testEntityManager.persistFlushFind(player);
        assertEquals(player.getPlayerId(),foundPlayer.getPlayerId());
        assertEquals(player.getSeason().getId(),foundPlayer.getSeason().getId());
    }

    @Test
    void findByIdPlayerTest(){
        Player player = new Player(101,"김갑환",season);
        testEntityManager.persistAndFlush(season);
        testEntityManager.persistAndFlush(player);
        Player foundPlayer = playerRepository.findById(101).orElse(null);
        assertEquals(player,foundPlayer);
        assertEquals(player.getPlayerName(),foundPlayer.getPlayerName());
        assertEquals(player.getSeason(),foundPlayer.getSeason());
    }

    @Test
    void dtoToEntityTest(){
        JSONObject jsonObject = new JSONObject().put("id",101).put("name","김갑환");
        PlayerDto.PlayerApiResponse playerDto = new PlayerDto.PlayerApiResponse(jsonObject);
        Player player = Player.builder(playerDto,season).build();
        assertEquals(playerDto.getPlayerId() , player.getPlayerId());
        assertEquals(playerDto.getPlayerName() , player.getPlayerName());
    }
}
