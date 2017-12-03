package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.DateUtils;
import com.htr.loan.Utils.DynamicSpecifications;
import com.htr.loan.Utils.SearchFilter;
import com.htr.loan.domain.BeidouRecord;
import com.htr.loan.domain.BeidouRenewal;
import com.htr.loan.domain.BeidouRenewal;
import com.htr.loan.domain.BeidouRepair;
import com.htr.loan.domain.LoanInfo;
import com.htr.loan.domain.SubLoanRecord;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.BeidouRecordRepository;
import com.htr.loan.domain.repository.BeidouRenewalRepository;
import com.htr.loan.domain.repository.BeidouRenewalRepository;
import com.htr.loan.domain.repository.BeidouRepairRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.BeidouRenewalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BeidouRenewalServiceImpl implements BeidouRenewalService {

    private static final Logger LOG = LoggerFactory.getLogger(BeidouRenewalServiceImpl.class);

    @Autowired
    private BeidouRenewalRepository beidouRenewalRepository;

    @Autowired
    private BeidouRecordRepository beidouRecordRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public BeidouRenewal saveBeidouRenewal(BeidouRenewal beidouRenewal) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_BEIDOURENEWAL, beidouRenewal.getBeidouRecord().getLicensePlate());
            log.setOperaType(Constants.OPERATYPE_ADD);
            //新装设置新卡号和老卡号相同
            BeidouRecord beidouRecord = beidouRenewal.getBeidouRecord();
            //换新卡
            if(beidouRenewal.getChangeCardType() == 0){
                beidouRecord.setBorrowCardFlow(false);
                beidouRecord.setOldCardNum(beidouRecord.getNewCardNum());
                beidouRenewal.setOldCardNum(beidouRecord.getNewCardNum());
                beidouRecord.setNewCardNum(beidouRenewal.getNewCardNum());
            } else if(beidouRenewal.getChangeCardType() == -1) {
                beidouRecord.setBorrowCardFlow(true);
                beidouRenewal.setOldCardNum(beidouRecord.getNewCardNum());
                beidouRecord.setNewCardNum(beidouRenewal.getNewCardNum());
            }
            //是否换终端
            if(beidouRenewal.isChangeTerminal()){
                beidouRenewal.setOldTerminal(beidouRecord.getTerminalNum());
                beidouRecord.setTerminalNum(beidouRenewal.getNewTerminal());
            }

            //更新剩余天数
            Date expirTime = org.apache.commons.lang3.time.DateUtils.addMonths(beidouRecord.getExpireTime(), beidouRenewal.getMonths());
            beidouRecord.setExpireTime(expirTime);
            beidouRecord.setLeftDays(DateUtils.between(beidouRecord.getExpireTime(), LocalDate.now()));
            beidouRecord = beidouRecordRepository.save(beidouRecord);
            beidouRenewal.setBeidouRecord(beidouRecord);
            beidouRenewal = beidouRenewalRepository.save(beidouRenewal);
            log.setRecordId(beidouRenewal.getUuid());
            systemLogRepository.save(log);
            return beidouRenewal;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update BeidouRenewal" + beidouRenewal.getBeidouRecord().getLicensePlate() + " fail!");
        }
        return null;
    }

    @Override
    public boolean removeBeidouRenewals(List<BeidouRenewal> beidouRenewalList) {
        try {
            SystemLog log;
            for (BeidouRenewal beidouRenewal : beidouRenewalList) {
                log = new SystemLog(Constants.MODULE_BEIDOURENEWAL, beidouRenewal.getBeidouRecord().getLicensePlate(),
                        beidouRenewal.getUuid(), Constants.OPERATYPE_DELETE);
                beidouRenewal.setActive(false);
                beidouRenewalRepository.save(beidouRenewal);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("delete BeidouRenewal fail!");
        }
        return false;
    }

    @Override
    public List<BeidouRenewal> findAllByBeidouRecord(String beidouRecordID) {
        BeidouRecord beidouRecord = beidouRecordRepository.findOne(beidouRecordID);
        if(null != beidouRecord){
            return beidouRenewalRepository.findAllByBeidouRecordAndActiveTrue(beidouRecord);
        }
        LOG.error("Cannot find the match beidouRecord by UUID" + beidouRecordID);
        return null;
    }

    @Override
    public Page<BeidouRenewal> findAll(Map<String, Object> filterParams, Pageable pageable) {
        Map<String, SearchFilter> filterMap = SearchFilter.parse(filterParams);
        return beidouRenewalRepository.findAll(DynamicSpecifications
                .bySearchFilter(filterMap.values(), BeidouRenewal.class), pageable);
    }
}
