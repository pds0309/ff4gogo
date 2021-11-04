package com.pds.schedule.task;


import com.pds.common.entity.Stat;
import com.pds.common.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class StatSchedule {

    private final RankerMatchUpdate rankerUpdate;

    private final StatRepository statRepository;

    private static final String POS = "{\"LM\":0,\"ST\":0,\"CF\":0,\"GK\":0,\"SW\":0,\"RW\":0,\"CM\":0,\"LW\":0,\"CDM\":0,\"CAM\":0,\"RB\":0,\"LB\":0,\"LWB\":0,\"RM\":0,\"RWB\":0,\"CB\":0}";

    @Scheduled(cron = "0 0 2 * * ?")
    public void cronStat() {
        updateStat();
        log.info("Stat 테이블 업데이트 완료");
        rankerUpdate.setRankerMatchZero();
    }

    public void updateStat() {
        List<Stat> updateRequiredStatList = rankerUpdate.getStatList();
        for (Stat currStat : updateRequiredStatList) {
            Optional<Stat> prevStat = statRepository.findById(currStat.getStatId());
            if (prevStat.isPresent()) {
                int cnt = prevStat.get().getCnt() + 1;
                Stat stat = Stat.builder(prevStat.get(), currStat , cnt).build();
                stat.setMostPos();
                statRepository.save(stat);
            } else {
                log.info(currStat.getStatId().getPlayer().getPlayerName() + currStat.getStatId().getMatchSid() + " is new Stat");
                currStat.setSpPosition(currStat.setPos(currStat.getSpPosition(),POS));
                currStat.setMostPos();
                statRepository.save(currStat);
            }
        }
    }
}
