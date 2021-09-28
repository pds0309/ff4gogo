package com.pds.common.repository;

import com.pds.common.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Integer> {
}
