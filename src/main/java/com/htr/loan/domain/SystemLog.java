package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class SystemLog extends BaseDomain {

    private String modules;//所属模块
    private String recordName;//数据记录名称
    private String recordId;//数据记录id
    private String operaType;//操作类型，可分为，增加、删除、修改、查询、接口调用等功能
    private String description;//描述

    public SystemLog(){}

    public SystemLog(String modules, String recordName){
        this.modules = modules;
        this.recordName = recordName;
    }

    public SystemLog(String modules, String recordName, String recordId, String operaType){
        this.modules = modules;
        this.recordId = recordId;
        this.recordName = recordName;
        this.operaType = operaType;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getOperaType() {
        return operaType;
    }

    public void setOperaType(String operaType) {
        this.operaType = operaType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
