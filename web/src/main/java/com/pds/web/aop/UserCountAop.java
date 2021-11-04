package com.pds.web.aop;


import com.pds.common.dto.UsersDto;
import com.pds.common.entity.UserCount;
import com.pds.common.entity.Users;
import com.pds.common.repository.UserCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
@RequiredArgsConstructor
@Log4j2
public class UserCountAop {
    private final UserCountRepository userCountRepository;


    @Around("execution(public * com.pds.web.service.UserSearchService.createUser(..))")
    public Object saveUserCounting(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();
            Users users = Users.builder((UsersDto.Info) result).build();
            UserCount userCount = userCountRepository
                    .findByUsers(users)
                    .orElse(new UserCount(users));
            userCount.updateTodayCountCount();
            userCountRepository.save(userCount);
        } catch (Throwable throwable) {
            log.info("[Can not count!] " + throwable.getMessage());
            return throwable;
        }
        return result;
    }
}
