package com.pds.common.repository;

import com.pds.common.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {
    Optional<Users> findByUserNameIsIgnoreCase(String nickname);
    Optional<Users> findByUserId(String id);
}
