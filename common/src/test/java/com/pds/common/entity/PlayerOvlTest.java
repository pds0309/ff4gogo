package com.pds.common.entity;


import com.pds.common.repository.PlayerOvlRepository;
import com.pds.common.repository.PlayerOvlWageOvlOnly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class PlayerOvlTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerOvlRepository overallRepository;



    private final PlayerOvl playerOvl = new PlayerOvl(101123,"CDM",105,25);
    @BeforeEach
    void setEntityManager(){
        entityManager.persistAndFlush(playerOvl);
    }

    @Test
    void createPlayerOverallTest(){
        PlayerOvl found = overallRepository.findById(101123).orElse(null);
        assertEquals(playerOvl,found);
    }

    @Test
    void findOverallByPlayerIdTest(){
        Optional<PlayerOvlWageOvlOnly> ovlWageOnly = overallRepository.findWageOvlByPlayerId(101123);
        ovlWageOnly.ifPresent(playerOvlWageOvlOnly -> assertEquals(105, playerOvlWageOvlOnly.getOvl()));
        ovlWageOnly.ifPresent(playerOvlWageOvlOnly -> assertEquals(25, playerOvlWageOvlOnly.getWage()));
    }

    @Test
    void findOverallNotExistPlayerTest(){
        Optional<PlayerOvlWageOvlOnly> ovlWageOnly = overallRepository.findWageOvlByPlayerId(1);
        int a = 5;
        if(!ovlWageOnly.isPresent()){
            a = 10;
        }
        assertEquals(10,a);
    }
}
