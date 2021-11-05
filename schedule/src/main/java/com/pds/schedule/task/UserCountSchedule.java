package com.pds.schedule.task;


import com.pds.common.repository.UserCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserCountSchedule {
    private final UserCountRepository userCountRepository;

    @Scheduled(cron = "0 0 15 * * ?")
    public void cronUserTodayCountAllZero(){
        log.info("유저 Today 초기화");
        userCountRepository.updateTodayCountZero();
    }
}
