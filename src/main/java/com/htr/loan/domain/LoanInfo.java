package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoanInfo extends BaseDomain {
    private String loanInfoNum; //档案号
    private Double LoanAmount; //贷款额
    private Date loanDate; //放款日期
    private Integer loansNum; //贷款期数
    private String RepaymentCard; //还款卡号
    private String cardHolder; //户名
    private String openBank; //开户行
    @OneToMany(cascade = CascadeType.ALL)
    private List<LoanRecord> loanRecords;
}
