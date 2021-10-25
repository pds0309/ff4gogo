package com.pds.schedule.task;


import com.pds.common.entity.Player;
import com.pds.common.entity.Stat;
import com.pds.common.entity.StatId;
import com.pds.common.repository.StatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class StatScheduleTest {


    @InjectMocks
    private StatSchedule statSchedule;
    @Mock
    private RankerMatchUpdate matchUpdate;
    @Mock
    private StatRepository statRepository;


    @Test
    void updateStatTest() {
        Stat stat1 = new Stat(new StatId(new Player(101123, "김갑환", null), 202109)
                , 1, 1, 3, 1, 1,"{}","ST");
        Stat stat2 = new Stat(new StatId(new Player(101124, "최번개", null), 202109)
                , 3, 5, 10, 1, 1,"{}","ST");
        Stat stat3 = new Stat(new StatId(new Player(101124, "최번개", null), 202107)
                , 1, 1, 5, 1, 1,"{}","ST");
        List<Stat> updateRequiredStatList = new ArrayList<>();
        updateRequiredStatList.add(stat1);
        updateRequiredStatList.add(stat2);
        updateRequiredStatList.add(stat3);

        given(matchUpdate.getStatList()).willReturn(updateRequiredStatList);
        Stat prevStat1 = new Stat(new StatId(new Player(101123, "김갑환", null), 202109)
                , 7, 10, 6.6, 2, 1,"{}","ST");
        given(statRepository.findById(updateRequiredStatList.get(0).getStatId()))
                .willReturn(Optional.of(prevStat1));
        given(statRepository.findById(updateRequiredStatList.get(1).getStatId()))
                .willReturn(Optional.of(updateRequiredStatList.get(2)));
        given(statRepository.findById(updateRequiredStatList.get(2).getStatId()))
                .willReturn(Optional.empty());
        statSchedule.updateStat();
        // 기존  Stat Table에 없는 경우
        verify(statRepository,times(1)).save(stat3);

        verify(statRepository,times(0)).save(stat2);
        verify(statRepository,times(0)).save(stat1);
        verify(statRepository,times(3)).save(any());

    }
}
