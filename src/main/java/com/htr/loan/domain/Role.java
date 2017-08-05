package com.htr.loan.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseDomain {

    private String roleName;
    private String description;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Collection<Resource> resources;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Resource> getResources() {
        return resources;
    }

    public void setResources(Collection<Resource> resources) {
        this.resources = resources;
    }
}
