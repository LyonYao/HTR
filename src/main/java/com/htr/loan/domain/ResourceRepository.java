package com.htr.loan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    List<Resource> findAllByActiveTrueAndParentResIsNull();

    Resource save(Resource resource);
}