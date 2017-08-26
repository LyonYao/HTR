package com.htr.loan.scheduler;

import com.htr.loan.Utils.DateUtils;
import com.htr.loan.Utils.LoanInfoHelper;
import com.htr.loan.domain.LoanInfo;
import com.htr.loan.domain.LoanRecord;
import com.htr.loan.domain.repository.LoanInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LoanInfoScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(LoanInfoScheduler.class);

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    public void checkLoanInfosNextRepay(){
        LOG.info("*********检查下次还款日期----开始***********");
        List<LoanInfo> loanInfos = loanInfoRepository.findAllByActiveTrueAndCompletedFalse();
        loanInfos.forEach(loanInfo -> {
            LoanRecord nextRepay = loanInfo.getNextRepay();
            if (null != nextRepay) {
                nextRepay = LoanInfoHelper.checkTheNextRepay(loanInfo);
                loanInfo.setNextRepay(nextRepay);
            }
            loanInfo.setLeftDays(DateUtils.between(nextRepay.getExpectDate(), LocalDate.now()));
        });
        loanInfoRepository.save(loanInfos);
        LOG.info("*********检查下次还款日期----结束***********");
    }
}
