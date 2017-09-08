package com.htr.loan.scheduler;

import com.htr.loan.Utils.DateUtils;
import com.htr.loan.Utils.LoanInfoHelper;
import com.htr.loan.domain.LoanInfo;
import com.htr.loan.domain.LoanRecord;
import com.htr.loan.domain.Vehicle;
import com.htr.loan.domain.repository.LoanInfoRepository;
import com.htr.loan.domain.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LoanSystemScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(LoanSystemScheduler.class);

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Scheduled(cron = "0 05 01 * * ?")
    public void checkLoanInfosNextRepay(){
        LOG.info("*********检查下次还款日期----开始***********");
        List<LoanInfo> loanInfos = loanInfoRepository.findAllByActiveTrueAndCompletedFalse();
        loanInfos.forEach(loanInfo -> {
            LoanRecord nextRepay = loanInfo.getNextRepay();
            if (null != nextRepay) {
                nextRepay = LoanInfoHelper.checkTheNextRepay(loanInfo);
                loanInfo.setNextRepay(nextRepay);
            }
            loanInfo.setLeftDays(DateUtils.between(nextRepay.getExpectDate(), LocalDate.now()));
        });
        loanInfoRepository.save(loanInfos);
        LOG.info("*********检查下次还款日期----结束***********");
    }

    @Scheduled(cron = "0 01 01 * * ?")
    public void checkVehicleInsuranceLeftDays(){
        LOG.info("*********检查保险到期天数----开始***********");
        List<Vehicle> vehicles = vehicleRepository.findAllByActiveTrue();
        vehicles.forEach(vehicle -> vehicle.setLeftDays(DateUtils.between(vehicle.getEndInsuranceTime(), LocalDate.now())));
        vehicleRepository.save(vehicles);
        LOG.info("*********检查保险到期天数----结束***********");
    }
}
