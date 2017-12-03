package com.htr.loan.domain.repository;

import com.htr.loan.domain.BeidouBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BeidouBranchRepository extends JpaRepository<BeidouBranch, String>, JpaSpecificationExecutor<BeidouBranch> {

    List<BeidouBranch> findAllByActiveTrue();
}
