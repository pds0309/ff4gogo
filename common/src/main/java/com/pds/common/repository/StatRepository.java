package com.pds.common.repository;

import com.pds.common.entity.Stat;
import com.pds.common.entity.StatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatRepository extends JpaRepository<Stat, StatId> {
    List<Stat> findTop5ByCntAfterAndStatIdMatchSidIsOrderByCntDesc(int cnt, int sid);
    List<Stat> findTop5ByCntAfterAndStatIdMatchSidIsOrderByGoalDesc(int cnt,int sid);
    List<Stat> findTop5ByCntAfterAndStatIdMatchSidIsOrderByStarDesc(int cnt,int sid);
    List<Stat> findTop5ByCntAfterAndStatIdMatchSidIsOrderByWinDesc(int cnt,int sid);

    @Query("select sum(s.cnt)/18 from Stat s where s.statId.matchSid=:mid")
    Integer findSeasonMatchNum(@Param("mid") int mid);

    @Query("select s.cnt from Stat s where s.statId.matchSid = :mid order by s.cnt")
    List<Integer> findCnt(@Param("mid") int mid);

    //select stat0_.match_sid as col_0_0_ from stat stat0_ order by stat0_.match_sid desc limit ?
    StatStatIdMatchSidOnly findTopStatIdMatchSidByOrderByStatIdMatchSidDesc();
}
