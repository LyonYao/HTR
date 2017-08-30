package com.htr.loan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoanRecord extends BaseDomain {
    private Integer loanNum; //贷款期数
    @JsonFormat(timezone = "GMT+8:00", pattern="yyyy-MM-dd")
    private Date expectDate; //应还款时间
    private Double expectMoney; //应还款额
    @JsonFormat(timezone = "GMT+8:00", pattern="yyyy-MM-dd")
    private Date actualDate; //实际还款时间(本期还款最后完成日期)
    private Long overdueDays; //逾期天数
    private boolean completed; //是否已还款完成
    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<SubLoanRecord> subLoanRecords; //多次还款记录
    private String description; //备注

    public Integer getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(Integer loanNum) {
        this.loanNum = loanNum;
    }

    public Date getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public Double getExpectMoney() {
        return expectMoney;
    }

    public void setExpectMoney(Double expectMoney) {
        this.expectMoney = expectMoney;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public Long getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Long overdueDays) {
        this.overdueDays = overdueDays;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<SubLoanRecord> getSubLoanRecords() {
        return subLoanRecords;
    }

    public void setSubLoanRecords(List<SubLoanRecord> subLoanRecords) {
        this.subLoanRecords = subLoanRecords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
