package com.htr.loan.domain.repository;

import com.htr.loan.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    List<Resource> findAllByActiveTrueAndParentResIsNull();

    Resource save(Resource resource);
}