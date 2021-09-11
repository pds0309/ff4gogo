package com.pds.common.repository;

import com.pds.common.entity.Stat;
import com.pds.common.entity.StatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<Stat, StatId> {

}
