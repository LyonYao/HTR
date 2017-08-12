package com.htr.loan.domain.repository;

import com.htr.loan.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}