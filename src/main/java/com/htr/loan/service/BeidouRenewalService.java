package com.htr.loan.service;

import com.htr.loan.domain.BeidouRenewal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BeidouRenewalService {

    BeidouRenewal saveBeidouRenewal(BeidouRenewal beidouRenewal);

    Page<BeidouRenewal> findAll(Map<String, Object> filterParams, Pageable pageable);

    boolean removeBeidouRenewals(List<BeidouRenewal> beidouRenewalList);

    List<BeidouRenewal> findAllByBeidouRecord(String beidouRecordID);
}
