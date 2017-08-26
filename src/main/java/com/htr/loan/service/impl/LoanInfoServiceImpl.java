package com.htr.loan.service.impl;

import com.htr.loan.Utils.*;
import com.htr.loan.domain.LoanInfo;
import com.htr.loan.domain.LoanRecord;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.LoanInfoRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.LoanInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoanInfoServiceImpl implements LoanInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(LoanInfoServiceImpl.class);

    @Autowired
    private LoanInfoRepository loanInfoRepository;
    
    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public LoanInfo saveLoanInfo(LoanInfo loanInfo) {
        try {

            SystemLog log = new SystemLog(Constants.MODULE_LOANINFO,loanInfo.getLoanInfoNum());
            if(StringUtils.isNotBlank(loanInfo.getUuid())){
                log.setOperaType(Constants.OPERATYPE_UPDATE);
            } else {
                log.setOperaType(Constants.OPERATYPE_ADD);
            }
            loanInfo = loanInfoRepository.save(loanInfo);
            LoanRecord nextRepay = LoanInfoHelper.checkTheNextRepay(loanInfo);
            loanInfo.setNextRepay(nextRepay);
            loanInfo.setLeftDays(DateUtils.between(nextRepay.getExpectDate(), LocalDate.now()));
            loanInfo = loanInfoRepository.save(loanInfo);
            log.setRecordId(loanInfo.getUuid());
            systemLogRepository.save(log);
            return loanInfo;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update LoanInfo" + loanInfo.getLoanInfoNum() + " fail!");
        }
        return null;
    }

    @Override
    public boolean removeLoanInfos(List<LoanInfo> loanInfoList) {
        try {
            SystemLog log;
            for(LoanInfo loanInfo : loanInfoList){
                log = new SystemLog(Constants.MODULE_LOANINFO, loanInfo.getLoanInfoNum(), loanInfo.getUuid(), Constants.OPERATYPE_DELETE);
                loanInfo.setActive(false);
                loanInfoRepository.save(loanInfo);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("delete LoanInfo fail!");
        }
        return false;
    }

    @Override
    public Page<LoanInfo> findAll(Map<String,Object> filterParams, Pageable pageable) {
        Map<String,SearchFilter> filterMap=SearchFilter.parse(filterParams);
        return loanInfoRepository.findAll(DynamicSpecifications
                .bySearchFilter(filterMap.values(), LoanInfo.class), pageable);
    }
}
