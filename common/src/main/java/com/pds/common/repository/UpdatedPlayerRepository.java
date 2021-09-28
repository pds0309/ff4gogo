package com.pds.common.repository;

import com.pds.common.entity.UpdatedPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UpdatedPlayerRepository extends JpaRepository<UpdatedPlayer, Integer> {
    @Query("select p.playerId from UpdatedPlayer p")
    List<Integer> findUpdatedPlayerId();
}
