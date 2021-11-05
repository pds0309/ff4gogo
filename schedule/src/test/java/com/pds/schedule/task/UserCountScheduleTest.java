package com.pds.schedule.task;


import com.pds.common.repository.UserCountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserCountSchedule.class})
class UserCountScheduleTest {
    @MockBean
    private UserCountRepository userCountRepository;
    @Autowired
    private UserCountSchedule userCountSchedule;
    @Test
    void test(){
        userCountSchedule.cronUserTodayCountAllZero();
        verify(userCountRepository,times(1)).updateTodayCountZero();
    }
}
