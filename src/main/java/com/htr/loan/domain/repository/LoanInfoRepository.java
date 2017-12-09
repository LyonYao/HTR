package com.htr.loan.domain.repository;

import com.htr.loan.domain.LoanInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface LoanInfoRepository extends JpaRepository<LoanInfo, String>, JpaSpecificationExecutor<LoanInfo> {

    LoanInfo save(LoanInfo loanInfo);

    List<LoanInfo> findAllByActiveTrueAndCompletedFalse();

    List<LoanInfo> findAllByActiveTrue();
}
