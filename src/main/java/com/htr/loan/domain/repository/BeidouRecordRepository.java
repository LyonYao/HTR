package com.htr.loan.domain.repository;

import com.htr.loan.domain.BeidouRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BeidouRecordRepository extends JpaRepository<BeidouRecord, String>, JpaSpecificationExecutor<BeidouRecord> {

}
