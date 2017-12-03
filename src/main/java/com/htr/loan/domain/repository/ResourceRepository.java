package com.htr.loan.domain.repository;

import com.htr.loan.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, String>, JpaSpecificationExecutor<Resource> {

    List<Resource> findAllByActiveTrueAndParentResIsNull();

    Resource findByResourceNameAndActiveTrue(String resourceName);
}