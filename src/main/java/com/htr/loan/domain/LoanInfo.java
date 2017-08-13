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
    private Person customer; //客户信息
    @OneToOne(cascade = CascadeType.MERGE)
    private Person surety; //担保人
    @OneToOne(cascade = CascadeType.MERGE)
    private Vehicle vehicle; //车辆信息
    private Double LoanAmount; //贷款额
    private Date loanDate; //放款日期
    private Integer loansNum; //贷款期数
    private String RepaymentCard; //还款卡号
    private String cardHolder; //户名
    private String openBank; //开户行
    @OneToMany(cascade = CascadeType.ALL)
    private List<LoanRecord> loanRecords; //还款记录
    private String receiptNum; //收据编号

    public String getLoanInfoNum() {
        return loanInfoNum;
    }

    public void setLoanInfoNum(String loanInfoNum) {
        this.loanInfoNum = loanInfoNum;
    }

    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
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

    public Double getLoanAmount() {
        return LoanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        LoanAmount = loanAmount;
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

    public String getRepaymentCard() {
        return RepaymentCard;
    }

    public void setRepaymentCard(String repaymentCard) {
        RepaymentCard = repaymentCard;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
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
}
