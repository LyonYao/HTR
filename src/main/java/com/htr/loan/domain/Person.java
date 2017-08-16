package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Person extends BaseDomain {
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PhoneInfo> phoneInfos;
    private String address;
    private String idNumber;
    private boolean surety;  //是否是担保人

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhoneInfo> getPhoneInfos() {
        return phoneInfos;
    }

    public void setPhoneInfos(List<PhoneInfo> phoneInfos) {
        this.phoneInfos = phoneInfos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public boolean isSurety() {
        return surety;
    }

    public void setSurety(boolean surety) {
        this.surety = surety;
    }
}
