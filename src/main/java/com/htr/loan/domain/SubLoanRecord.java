package com.htr.loan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class SubLoanRecord extends BaseDomain {

    @OneToOne
    private User payee;  //收款人
    private Double receipts; //收到的金额

    @JsonFormat(timezone = "GMT+8:00", pattern="yyyy-MM-dd")
    private Date receiptDate; //收款时间
    @OneToOne
    private BankCard bankCard; //收款银行卡
    private String description; //备注

    public User getPayee() {
        return payee;
    }

    public void setPayee(User payee) {
        this.payee = payee;
    }

    public Double getReceipts() {
        return receipts;
    }

    public void setReceipts(Double receipts) {
        this.receipts = receipts;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
