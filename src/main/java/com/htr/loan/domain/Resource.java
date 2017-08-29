package com.htr.loan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"parentRes"})
public class Resource extends BaseDomain {
    private String resourceName; //资源名称
    private String resourceType; //资源类型
    private String resPath; //资源Path

    @ManyToOne
    private Resource parentRes; //资源父ID

    @OneToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Resource> childrenRes;

    @Transient
    private boolean selected;

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

    public List<Resource> getChildrenRes() {
        return childrenRes;
    }

    public void setChildrenRes(List<Resource> childrenRes) {
        this.childrenRes = childrenRes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
