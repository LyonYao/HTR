package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Resource extends BaseDomain {
    private String resourceName; //资源名称
    private String resourceType; //资源类型
    private String resPath; //资源Path

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Resource parentRes; //资源父ID

    @OneToMany(cascade = CascadeType.MERGE)
    private Collection<Resource> childrenRes;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResPath() {
        return resPath;
    }

    public void setResPath(String resPath) {
        this.resPath = resPath;
    }

    public Resource getParentRes() {
        return parentRes;
    }

    public void setParentRes(Resource parentRes) {
        this.parentRes = parentRes;
    }

    public Collection<Resource> getChildrenRes() {
        return childrenRes;
    }

    public void setChildrenRes(Collection<Resource> childrenRes) {
        this.childrenRes = childrenRes;
    }
}
