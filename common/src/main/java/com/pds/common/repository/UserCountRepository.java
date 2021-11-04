package com.pds.common.repository;

import com.pds.common.entity.UserCount;
import com.pds.common.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserCountRepository extends JpaRepository<UserCount,Long> {
    Optional<UserCount> findByUsers(Users users);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserCount u set u.todayCount = 0")
    void updateTodayCountZero();
}
