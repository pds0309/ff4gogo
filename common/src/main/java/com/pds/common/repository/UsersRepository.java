package com.pds.common.repository;

import com.pds.common.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
    Users findByUserNameIsIgnoreCase(String nickname);
}
