package com.pds.schedule.task;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PlayerSchedule.class})
class PlayerScheduleTest {
    @MockBean
    private PlayerUpdate playerUpdate;
    @Autowired
    private PlayerSchedule playerSchedule;

    @Test
    void test() {
        playerSchedule.cronPlayers();
        verify(playerUpdate,times(1)).getNewPlayerList();
        verify(playerUpdate,times(1)).updatePlayers(anyList());
        verify(playerUpdate,times(1)).updateSeason();
    }
}
