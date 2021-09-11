package com.pds.common.entity;


import com.pds.common.repository.RankerMatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RankerMatchTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RankerMatchRepository rankerMatchRepository;

    @Test
    void findRankerMatchByIdTest(){
        RankerMatchId rankerMatchId = new RankerMatchId("ABCD","USER1");
        RankerMatch rankerMatch = new RankerMatch(rankerMatchId,0);
        testEntityManager.persistAndFlush(rankerMatch);
        RankerMatch foundRankerMatch = rankerMatchRepository.findById(rankerMatchId).orElse(null);
        assertNotNull(foundRankerMatch);
        assertEquals(foundRankerMatch.getRankerMatchId(),rankerMatchId);
        assertEquals(foundRankerMatch.getRankerMatchId().getUserId(),rankerMatchId.getUserId());
        assertEquals(foundRankerMatch.getMatchHit(),rankerMatch.getMatchHit());
    }

}
