package com.htr.loan.domain.repository;

import com.htr.loan.domain.BeidouBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BeidouBranchRepository extends JpaRepository<BeidouBranch, String>, JpaSpecificationExecutor<BeidouBranch> {

}
