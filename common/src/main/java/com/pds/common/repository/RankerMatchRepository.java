package com.pds.common.repository;

import com.pds.common.entity.RankerMatch;
import com.pds.common.entity.RankerMatchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RankerMatchRepository extends JpaRepository<RankerMatch , RankerMatchId> {
    @Query("select r.rankerMatchId.matchCode from RankerMatch r where r.rankerMatchId.userId=:userid")
    List<String> findAllMatchCodeByUserId(@Param("userid") String userid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from RankerMatch where matchHit = 0 and rankerMatchId.userId=:userid")
    void deleteMatchesNotUsed(@Param("userid") String userid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update RankerMatch r set r.matchHit = 0 where r.matchHit = 1")
    void updateAllZero();
}
