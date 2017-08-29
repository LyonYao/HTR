package com.htr.loan.service;

import com.htr.loan.domain.LoanInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface LoanInfoService {

    LoanInfo saveLoanInfo(LoanInfo loanInfo);

    LoanInfo repayment(LoanInfo loanInfo);

    Page<LoanInfo> findAll(Map<String,Object> filterParams, Pageable pageable);

    boolean removeLoanInfos(List<LoanInfo> loanInfoList);
}
