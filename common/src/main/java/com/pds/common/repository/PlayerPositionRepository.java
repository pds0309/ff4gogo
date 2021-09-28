package com.pds.common.repository;

import com.pds.common.entity.PlayerPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerPositionRepository extends JpaRepository<PlayerPosition , Integer> {
}
