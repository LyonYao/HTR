package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.xml.crypto.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoanRecord extends BaseDomain {
    private Integer loanNum; //贷款期数
    private Data expectDate; //应还款时间
    private Double expectMoney; //应还款时间
    private Data actualDate; //实际还款时间
    private Double actualMoney; //实际还款时间
    private Integer overdue; //逾期天数
    private Double balance; //余额
    private Data payee; //收款人
    private String description; //应还款时间
}
