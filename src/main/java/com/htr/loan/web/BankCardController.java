package com.htr.loan.web;

import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.BankCard;
import com.htr.loan.service.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bankCard")
public class BankCardController {

    @Autowired
    private BankCardService bankCardService;

    @RequestMapping(value = "/{active}", method = RequestMethod.GET)
    public List<BankCard> findAllBankCard(@PathVariable int active){
        if (active == 0){
            return bankCardService.findAllBankCard();
        } else return bankCardService.findAllByActiveTrue();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public BankCard saveBankCard(@RequestBody BankCard bankCard){
        return bankCardService.saveBankCard(bankCard);
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> stopBankCard(@RequestBody List<BankCard> bankCards){
        boolean isDeleted = bankCardService.activeOrStopBankCards(bankCards, false);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

    @RequestMapping(value = "active", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> activeBankCard(@RequestBody List<BankCard> bankCards){
        boolean isDeleted = bankCardService.activeOrStopBankCards(bankCards, true);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }
}
