package com.pds.schedule.task;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class PlayerSchedule {

    private final PlayerUpdate playerUpdate;

    @Scheduled(cron = "0 4 0 * * FRI")
    public void cronPlayers(){
        playerUpdate.updateSeason();
        playerUpdate.updatePlayers(playerUpdate.getNewPlayerList());
    }

}
