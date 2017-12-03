package com.htr.loan.service;


import com.htr.loan.domain.BeidouBranch;

import java.util.List;

public interface BeidouBranchService {

    List<BeidouBranch> findAllBeidouBranch();

    List<BeidouBranch> findAllByActiveTrue();

    BeidouBranch saveBeidouBranch(BeidouBranch beidouBranch);

    boolean activeOrStopBeidouBranchs(List<BeidouBranch> beidouBranchs, boolean flag);
}
