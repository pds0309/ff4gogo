package com.pds.web.aop;

import com.pds.common.dto.UsersDto;
import com.pds.common.repository.UserCountRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserCountAop.class)
class UserCountAopTest {

    @MockBean
    private ProceedingJoinPoint joinPoint;

    @MockBean
    private UserCountRepository userCountRepository;

    @Autowired
    private UserCountAop userCountAop;


    @Test
    void saveUserCountingTest() throws Throwable {
        Object o = new UsersDto.Info("김","ID",100,200);
        given(joinPoint.proceed()).willReturn(o);
        userCountAop.saveUserCounting(joinPoint);
        verify(userCountRepository,times(1)).save(any());
    }

    @Test
    void saveUserCountingNullTest() throws Throwable{
        given(joinPoint.proceed()).willReturn(null);
        assertEquals(NullPointerException.class,userCountAop.saveUserCounting(joinPoint).getClass());
        verify(userCountRepository,times(0)).save(any());

    }
    @Test
    void saveUserCountingInvalidTest() throws Throwable{
        Object o = new JSONObject().put("hi",1);
        given(joinPoint.proceed()).willReturn(o);
        userCountAop.saveUserCounting(joinPoint);
        verify(userCountRepository,times(0)).save(any());
    }
    @Test
    void savUserCountingNotExistUsersTest() throws Throwable {
        Object o = new UsersDto.Info("김","ID",100,200);
        given(joinPoint.proceed()).willReturn(o);
        given(userCountRepository.findByUsers(any())).willThrow(new UnsatisfiedDependencyException("","","",""));
        userCountAop.saveUserCounting(joinPoint);
        verify(userCountRepository,times(0)).save(any());
    }
}
