package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.DateUtils;
import com.htr.loan.Utils.DynamicSpecifications;
import com.htr.loan.Utils.LoanInfoHelper;
import com.htr.loan.Utils.MoneyCalculator;
import com.htr.loan.Utils.SearchFilter;
import com.htr.loan.domain.LoanInfo;
import com.htr.loan.domain.LoanRecord;
import com.htr.loan.domain.SubLoanRecord;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.LoanInfoRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.LoanInfoService;
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
public class LoanInfoServiceImpl implements LoanInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(LoanInfoServiceImpl.class);

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public LoanInfo saveLoanInfo(LoanInfo loanInfo) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_LOANINFO, loanInfo.getLoanInfoNum());
            log.setOperaType(Constants.OPERATYPE_ADD);
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
    public LoanInfo repayment(LoanInfo loanInfo) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_LOANINFO, loanInfo.getLoanInfoNum(), loanInfo.getUuid(), Constants.OPERATYPE_REPAYMENT);

            LoanRecord nextRepay = loanInfo.getNextRepay();
            double total = 0d;
            Date repayDate = null;
            for (SubLoanRecord subLoanRecord : nextRepay.getSubLoanRecords()) {
                total = MoneyCalculator.add(total, subLoanRecord.getReceipts());
                //获取最后一次还款日期
                if (subLoanRecord.getUuid() == null) {
                    repayDate = subLoanRecord.getReceiptDate();
                }
            }
            total = MoneyCalculator.add(total, loanInfo.getBalance());

            boolean isExecute = false;
            //多次还款叠加大于本期需要还款的钱数
            while (total >= nextRepay.getExpectMoney()) {
                isExecute = true;
                total = MoneyCalculator.subtract(total, nextRepay.getExpectMoney());
                nextRepay.setCompleted(true);
                nextRepay.setActualDate(repayDate);
                loanInfo.setBalance(total);

                //从列表中找出这次还款记录并替换
                List<LoanRecord> loanRecords = loanInfo.getLoanRecords();
                for (int i = 0; i < loanRecords.size(); i++) {
                    if (loanRecords.get(i).getUuid().equals(nextRepay.getUuid())){
                        loanRecords.set(i, nextRepay);
                        break;
                    }
                }

                loanInfo.setLoanRecords(loanRecords);

                nextRepay = LoanInfoHelper.checkTheNextRepay(loanInfo);
                if (null == nextRepay) {
                    loanInfo.setCompleted(true);
                    loanInfo.setNextRepay(null);
                    break;
                } else {
                    loanInfo.setNextRepay(nextRepay);
                }
            }

            if(!isExecute){
                loanInfo.setBalance(total);
                //从列表中找出这次还款记录并替换
                List<LoanRecord> loanRecords = loanInfo.getLoanRecords();
                for (int i = 0; i < loanRecords.size(); i++) {
                    if (loanRecords.get(i).getUuid().equals(nextRepay.getUuid())){
                        loanRecords.set(i, nextRepay);
                        break;
                    }
                }
            }

            //计算应还款总额
            double totalBalance = loanInfo.getTotalRepayment();
            for (LoanRecord loanRecord : loanInfo.getLoanRecords()) {
                if (loanRecord.isCompleted()){
                    totalBalance = MoneyCalculator.subtract(totalBalance, loanRecord.getExpectMoney());
                }
            }
            loanInfo.setTotalBalance(MoneyCalculator.subtract(totalBalance, loanInfo.getBalance()));

            //计算下期还款天数
            if(loanInfo.isCompleted()){
                loanInfo.setLeftDays(0);
            }else {
                loanInfo.setLeftDays(DateUtils.between(loanInfo.getNextRepay().getExpectDate(), LocalDate.now()));
            }
            loanInfo = loanInfoRepository.save(loanInfo);
            systemLogRepository.save(log);
            return loanInfo;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("repayment LoanInfo" + loanInfo.getLoanInfoNum() + " fail!");
        }
        return null;
    }

    @Override
    public boolean removeLoanInfos(List<LoanInfo> loanInfoList) {
        try {
            SystemLog log;
            for (LoanInfo loanInfo : loanInfoList) {
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
    public Page<LoanInfo> findAll(Map<String, Object> filterParams, Pageable pageable) {
        Map<String, SearchFilter> filterMap = SearchFilter.parse(filterParams);
        return loanInfoRepository.findAll(DynamicSpecifications
                .bySearchFilter(filterMap.values(), LoanInfo.class), pageable);
    }
}
