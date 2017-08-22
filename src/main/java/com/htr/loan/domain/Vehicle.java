package com.htr.loan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Vehicle extends BaseDomain {
    private String brand;  //车辆品牌
    @OneToOne(cascade = CascadeType.MERGE)
    private Person holder;  //所有人
    private String licensePlate;  //车牌号
    private String frameNumber;  //车架号
    private Double evaluation; //预估价
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date registrationDate; //上户时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startInsuranceTime; //开始保险时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endInsuranceTime; //保险到期时间
    private boolean detain; //是否被扣留

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Person getHolder() {
        return holder;
    }

    public void setHolder(Person holder) {
        this.holder = holder;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public Double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Double evaluation) {
        this.evaluation = evaluation;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getStartInsuranceTime() {
        return startInsuranceTime;
    }

    public void setStartInsuranceTime(Date startInsuranceTime) {
        this.startInsuranceTime = startInsuranceTime;
    }

    public Date getEndInsuranceTime() {
        return endInsuranceTime;
    }

    public void setEndInsuranceTime(Date endInsuranceTime) {
        this.endInsuranceTime = endInsuranceTime;
    }

    public boolean isDetain() {
        return detain;
    }

    public void setDetain(boolean detain) {
        this.detain = detain;
    }
}
