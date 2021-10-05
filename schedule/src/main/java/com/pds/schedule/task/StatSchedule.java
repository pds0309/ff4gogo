package com.pds.schedule.task;


import com.pds.common.entity.Stat;
import com.pds.common.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class StatSchedule {

    private final RankerMatchUpdate rankerUpdate;

    private final StatRepository statRepository;

    @Scheduled(cron = "0 0 5 * * ?")
    public void cronStat(){
        updateStat();
        log.info("Stat 테이블 업데이트 완료");
        rankerUpdate.setRankerMatchZero();
    }

    public void updateStat(){
        List<Stat> updateRequiredStatList = rankerUpdate.getStatList();
        for (Stat currStat : updateRequiredStatList) {
            Optional<Stat> prevStat = statRepository.findById(currStat.getStatId());
            if (prevStat.isPresent()) {
                int cnt = prevStat.get().getCnt() + 1;
                statRepository.save(Stat.builder(prevStat.get(), currStat, cnt).build());
            } else {
                log.info(currStat.getStatId().getPlayer().getPlayerName() + currStat.getStatId().getMatchSid() + " is new Stat");
                statRepository.save(currStat);
            }
        }
    }
}
