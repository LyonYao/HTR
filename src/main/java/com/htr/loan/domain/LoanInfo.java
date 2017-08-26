package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoanInfo extends BaseDomain {
    private String loanInfoNum; //档案号
    @OneToOne(cascade = CascadeType.MERGE)
    private Person surety; //担保人
    @OneToOne(cascade = CascadeType.MERGE)
    private Vehicle vehicle; //车辆信息
    @OneToOne(cascade = CascadeType.MERGE)
    private BankCard bankCard; //银行卡
    private Double LoanAmount; //贷款额
    private Double totalRepayment; //应还款总额
    private Double balance; //上期多还余额
    private Double totalBalance; //总余额
    private Date loanDate; //放款日期
    private Integer loansNum; //贷款期数
    @OneToMany(cascade = CascadeType.ALL)
    private List<LoanRecord> loanRecords; //还款记录
    private String receiptNum; //收据编号
    @OneToOne(cascade = CascadeType.MERGE)
    private LoanRecord nextRepay;//下次还款记录
    private long leftDays;//剩余还款天数(可以不要这个属性,在前台动态计算. 但是不要这个属性不好实现按逾期天数排序)
    private boolean completed; //是否已完成所有还款

    public String getLoanInfoNum() {
        return loanInfoNum;
    }

    public void setLoanInfoNum(String loanInfoNum) {
        this.loanInfoNum = loanInfoNum;
    }

    public Person getSurety() {
        return surety;
    }

    public void setSurety(Person surety) {
        this.surety = surety;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public Double getLoanAmount() {
        return LoanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        LoanAmount = loanAmount;
    }

    public Double getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(Double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Integer getLoansNum() {
        return loansNum;
    }

    public void setLoansNum(Integer loansNum) {
        this.loansNum = loansNum;
    }

    public List<LoanRecord> getLoanRecords() {
        return loanRecords;
    }

    public void setLoanRecords(List<LoanRecord> loanRecords) {
        this.loanRecords = loanRecords;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public LoanRecord getNextRepay() {
        return nextRepay;
    }

    public void setNextRepay(LoanRecord nextRepay) {
        this.nextRepay = nextRepay;
    }

    public long getLeftDays() {
        return leftDays;
    }

    public void setLeftDays(long leftDays) {
        this.leftDays = leftDays;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
