package com.pds.common.repository;

import com.pds.common.entity.RankerMatch;
import com.pds.common.entity.RankerMatchId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankerMatchRepository extends JpaRepository<RankerMatch , RankerMatchId> {

}
