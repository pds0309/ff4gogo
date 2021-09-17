package com.pds.common.repository;

import com.pds.common.entity.PlayerOvl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlayerOvlRepository extends JpaRepository<PlayerOvl, Integer> {
    Optional<PlayerOvlWageOvlOnly> findWageOvlByPlayerId(int playerId);

}
