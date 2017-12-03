package com.htr.loan.domain.repository;

import com.htr.loan.domain.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemLogRepository extends JpaRepository<SystemLog, String>, JpaSpecificationExecutor<SystemLog> {

}