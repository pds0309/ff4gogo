package com.pds.common.entity;


import com.pds.common.dto.SeasonDto;
import com.pds.common.repository.SeasonRepository;
import org.hibernate.PropertyValueException;
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
class SeasonTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SeasonRepository seasonRepository;

    private final Season season = new Season(1,"1","시즌1","1url");

    @Test
    void createSeasonNoArgExceptionTest(){
        Season season2 = new Season();
        assertThrows(PersistenceException.class,()->testEntityManager.persistFlushFind(season2));
    }

    @Test
    void findByIdSeasonTest(){
        testEntityManager.persist(season);
        testEntityManager.flush();
        Season foundSeason = seasonRepository.findById(season.getId()).orElse(null);
        assertEquals(season.getName(),foundSeason.getName());
        assertEquals(season.getId(),foundSeason.getId());
        assertEquals(season.getImg(),foundSeason.getImg());
        assertEquals(season.getInfo(),foundSeason.getInfo());
    }

    @Test
    void dtoToEntityTest(){
        SeasonDto seasonDto = new SeasonDto(1,"1","시즌1","1url");
        Season season = Season.builder(seasonDto).build();
        assertEquals(season.getId(),seasonDto.getId());
    }

    @Test
    void entityToDtoTest(){
        SeasonDto seasonDto = SeasonDto.builder(season).build();
        assertEquals(season.getId(),seasonDto.getId());
    }
}
