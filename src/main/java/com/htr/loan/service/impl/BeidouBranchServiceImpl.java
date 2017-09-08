package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.DateUtils;
import com.htr.loan.Utils.MoneyCalculator;
import com.htr.loan.domain.BeidouBranch;
import com.htr.loan.domain.BeidouRecord;
import com.htr.loan.domain.BeidouRenewal;
import com.htr.loan.domain.BeidouRepair;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.BeidouBranchRepository;
import com.htr.loan.domain.repository.BeidouRecordRepository;
import com.htr.loan.domain.repository.BeidouRenewalRepository;
import com.htr.loan.domain.repository.BeidouRepairRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.BeidouBranchService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BeidouBranchServiceImpl implements BeidouBranchService {

    private static final Logger LOG = LoggerFactory.getLogger(BeidouBranchServiceImpl.class);

    @Autowired
    private BeidouBranchRepository beidouBranchRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private BeidouRecordRepository beidouRecordRepository;

    @Autowired
    private BeidouRenewalRepository beidouRenewalRepository;

    @Autowired
    private BeidouRepairRepository beidouRepairRepository;

    @Override
    public List<BeidouBranch> findAllBeidouBranch() {

        List<BeidouBranch> beidouBranchs = beidouBranchRepository.findAll();
        beidouBranchs.forEach(beidouBranch -> {
            List<BeidouRecord> beidouRecords = beidouRecordRepository.findAllByBeidouBranchAndActiveTrue(beidouBranch);
            List<BeidouRenewal> beidouRenewals = beidouRenewalRepository.findAllByBeidouBranchAndActiveTrue(beidouBranch);
            List<BeidouRepair> beidouRepairs = beidouRepairRepository.findAllByBeidouBranchAndActiveTrue(beidouBranch);
            Double dailyIncome = 0d;
            Double monthlyIncome = 0d;
            Double annualIncome = 0d;
            Double totalIncome = 0d;
            //叠加新装金额
            for (BeidouRecord beidouRecord: beidouRecords) {
                if(DateUtils.isToday(beidouRecord.getJoinTime())){
                    dailyIncome = MoneyCalculator.add(dailyIncome, beidouRecord.getInstallationFee());
                }

                if(DateUtils.isThisMonth(beidouRecord.getJoinTime())){
                    monthlyIncome = MoneyCalculator.add(monthlyIncome, beidouRecord.getInstallationFee());
                }

                if(DateUtils.isThisYear(beidouRecord.getJoinTime())){
                    annualIncome = MoneyCalculator.add(annualIncome, beidouRecord.getInstallationFee());
                }
                totalIncome = MoneyCalculator.add(totalIncome, beidouRecord.getInstallationFee());
            }
            //叠加续费金额
            for (BeidouRenewal beidouRenewal: beidouRenewals) {
                if(DateUtils.isToday(beidouRenewal.getRenewalDate())){
                    dailyIncome = MoneyCalculator.add(dailyIncome, beidouRenewal.getRenewalFee());
                }

                if(DateUtils.isThisMonth(beidouRenewal.getRenewalDate())){
                    monthlyIncome = MoneyCalculator.add(monthlyIncome, beidouRenewal.getRenewalFee());
                }

                if(DateUtils.isThisYear(beidouRenewal.getRenewalDate())){
                    annualIncome = MoneyCalculator.add(annualIncome, beidouRenewal.getRenewalFee());
                }
                totalIncome = MoneyCalculator.add(totalIncome, beidouRenewal.getRenewalFee());
            }

            //叠加维修金额
            for (BeidouRepair beidouRepair: beidouRepairs) {
                if(DateUtils.isToday(beidouRepair.getRepairDate())){
                    dailyIncome = MoneyCalculator.add(dailyIncome, beidouRepair.getRepairFee());
                }

                if(DateUtils.isThisMonth(beidouRepair.getRepairDate())){
                    monthlyIncome = MoneyCalculator.add(monthlyIncome, beidouRepair.getRepairFee());
                }

                if(DateUtils.isThisYear(beidouRepair.getRepairDate())){
                    annualIncome = MoneyCalculator.add(annualIncome, beidouRepair.getRepairFee());
                }
                totalIncome = MoneyCalculator.add(totalIncome, beidouRepair.getRepairFee());
            }

            beidouBranch.setDailyIncome(dailyIncome);
            beidouBranch.setMonthlyIncome(monthlyIncome);
            beidouBranch.setAnnualIncome(annualIncome);
            beidouBranch.setTotalIncome(totalIncome);
        });
        return beidouBranchs;
    }

    @Override
    public List<BeidouBranch> findAllByActiveTrue() {
        return beidouBranchRepository.findAllByActiveTrue();
    }

    @Override
    public BeidouBranch saveBeidouBranch(BeidouBranch beidouBranch) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_BEIDOUBRANCH, beidouBranch.getBranchName());
            if (StringUtils.isNotBlank(beidouBranch.getUuid())) {
                log.setOperaType(Constants.OPERATYPE_UPDATE);
            } else {
                log.setOperaType(Constants.OPERATYPE_ADD);
            }
            beidouBranch = beidouBranchRepository.save(beidouBranch);
            log.setRecordId(beidouBranch.getUuid());
            systemLogRepository.save(log);
            return beidouBranch;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update beidouBranch " + beidouBranch.getBranchName() + " fail!");
        }
        return null;
    }

    @Override
    public boolean activeOrStopBeidouBranchs(List<BeidouBranch> beidouBranchs, boolean flag) {
        try {
            SystemLog log;
            for (BeidouBranch beidouBranch : beidouBranchs) {
                if(flag)
                    log = new SystemLog(Constants.MODULE_BEIDOUBRANCH, beidouBranch.getBranchName(), beidouBranch.getUuid(), Constants.OPERATYPE_ACTIVE);
                else log = new SystemLog(Constants.MODULE_BEIDOUBRANCH, beidouBranch.getBranchName(), beidouBranch.getUuid(), Constants.OPERATYPE_STOP);
                beidouBranch.setActive(flag);
                beidouBranchRepository.save(beidouBranch);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("active BeidouBranch fail!");
        }
        return false;
    }
}
