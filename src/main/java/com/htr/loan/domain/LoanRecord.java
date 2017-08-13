package com.htr.loan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoanRecord extends BaseDomain {
    private Integer loanNum; //贷款期数
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expectDate; //应还款时间
    private Double expectMoney; //应还款时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date actualDate; //实际还款时间
    private Double actualMoney; //实际还款时间
    private Integer overdue; //逾期天数
    private Double balance; //余额
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date payee; //收款人
    private String description; //应还款时间

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

    public Double getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(Double actualMoney) {
        this.actualMoney = actualMoney;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getPayee() {
        return payee;
    }

    public void setPayee(Date payee) {
        this.payee = payee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
