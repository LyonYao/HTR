package com.htr.loan.Utils;

import com.htr.loan.domain.LoanInfo;
import com.htr.loan.domain.LoanRecord;

public abstract class LoanInfoHelper {

    /**
     * 根据当前档案, 查询下次还款的记录
     * @param loanInfo
     * @return
     */
    public static LoanRecord checkTheNextRepay(LoanInfo loanInfo){
        LoanRecord nextRepay = null;
        for(LoanRecord loanRecord : loanInfo.getLoanRecords()) {
            if(loanRecord.isCompleted()) continue;
            if(null != nextRepay){
                if(DateUtils.between(nextRepay.getExpectDate(), loanRecord.getExpectDate()) > 0){
                    nextRepay = loanRecord;
                }
            } else {
                nextRepay = loanRecord;
            }
        }
        return nextRepay;
    }
}
