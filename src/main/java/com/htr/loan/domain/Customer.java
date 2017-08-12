package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Customer extends BaseDomain {
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PhoneInfo> phoneInfos;
    private String address;
    private String idNumber;
}
