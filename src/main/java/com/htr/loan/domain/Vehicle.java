package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Vehicle extends BaseDomain {
    private String brand;
    private String LicensePlate;
    private String frameNumber;
    private Double evaluation;
}
