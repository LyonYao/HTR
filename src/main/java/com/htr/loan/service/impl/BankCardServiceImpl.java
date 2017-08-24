package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.DateUtils;
import com.htr.loan.Utils.MoneyCalculator;
import com.htr.loan.domain.BankCard;
import com.htr.loan.domain.SubLoanRecord;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.BankCardRepository;
import com.htr.loan.domain.repository.SubLoanRecordRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.BankCardService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BankCardServiceImpl implements BankCardService {

    private static final Logger LOG = LoggerFactory.getLogger(BankCardServiceImpl.class);

    @Autowired
    private BankCardRepository bankCardRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private SubLoanRecordRepository subLoanRecordRepository;

    @Override
    public List<BankCard> findAllBankCard() {

        List<BankCard> bankCards = bankCardRepository.findAll();
        bankCards.forEach(bankCard -> {
            List<SubLoanRecord> subLoanRecords = subLoanRecordRepository.findAllByBankCardAndActiveTrue(bankCard);
            Double dailyIncome = 0d;
            Double monthlyIncome = 0d;
            Double annualIncome = 0d;
            Double totalIncome = 0d;
            for (SubLoanRecord subLoanRecord: subLoanRecords) {
                if(DateUtils.isToday(subLoanRecord.getReceiptDate())){
                    dailyIncome = MoneyCalculator.add(dailyIncome, subLoanRecord.getReceipts());
                }

                if(DateUtils.isThisMonth(subLoanRecord.getReceiptDate())){
                    monthlyIncome = MoneyCalculator.add(monthlyIncome, subLoanRecord.getReceipts());
                }

                if(DateUtils.isThisYear(subLoanRecord.getReceiptDate())){
                    annualIncome = MoneyCalculator.add(annualIncome, subLoanRecord.getReceipts());
                }
                totalIncome = MoneyCalculator.add(totalIncome, subLoanRecord.getReceipts());
            }
            bankCard.setDailyIncome(dailyIncome);
            bankCard.setMonthlyIncome(monthlyIncome);
            bankCard.setAnnualIncome(annualIncome);
            bankCard.setTotalIncome(totalIncome);
        });
        return bankCards;
    }

    @Override
    public List<BankCard> findAllByActiveTrue() {
        return bankCardRepository.findAllByActiveTrue();
    }

    @Override
    public BankCard saveBankCard(BankCard bankCard) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_BANKCARD, bankCard.getCardNumber());
            if (StringUtils.isNotBlank(bankCard.getUuid())) {
                log.setOperaType(Constants.OPERATYPE_UPDATE);
            } else {
                log.setOperaType(Constants.OPERATYPE_ADD);
            }
            bankCard = bankCardRepository.save(bankCard);
            log.setRecordId(bankCard.getUuid());
            systemLogRepository.save(log);
            return bankCard;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update bankCard " + bankCard.getCardNumber() + " fail!");
        }
        return null;
    }

    @Override
    public boolean activeOrStopBankCards(List<BankCard> bankCards, boolean flag) {
        try {
            SystemLog log;
            for (BankCard bankCard : bankCards) {
                if(flag)
                    log = new SystemLog(Constants.MODULE_BANKCARD, bankCard.getCardNumber(), bankCard.getUuid(), Constants.OPERATYPE_ACTIVE);
                else log = new SystemLog(Constants.MODULE_BANKCARD, bankCard.getCardNumber(), bankCard.getUuid(), Constants.OPERATYPE_STOP);
                bankCard.setActive(flag);
                bankCardRepository.save(bankCard);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("active BankCard fail!");
        }
        return false;
    }
}
