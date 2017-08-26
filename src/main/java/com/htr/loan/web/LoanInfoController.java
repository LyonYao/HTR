package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.LoanInfo;
import com.htr.loan.service.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loanInfo")
public class LoanInfoController {

    @Autowired
    private LoanInfoService loanInfoService;

    @RequestMapping(value = "/{currentPage}/{pageSize}", method = RequestMethod.GET)
    public Page<LoanInfo> findAll(@PathVariable int currentPage,
                                @PathVariable int pageSize,
                                @RequestParam(defaultValue = "{}") String jsonFilter){
        Map<String, Object> filterParams = WebUtil.getParametersStartingWith(jsonFilter, Constants.SEARCH_PREFIX);
        filterParams.put("EQ_active", Constants.RECORD_EXIST);

        return loanInfoService.findAll(filterParams, WebUtil.buildPageRequest(currentPage, pageSize));
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public LoanInfo saveLoanInfo(@RequestBody LoanInfo loanInfo){
        return loanInfoService.saveLoanInfo(loanInfo);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> removeLoanInfo(@RequestBody List<LoanInfo> loanInfos){
        boolean isDeleted = loanInfoService.removeLoanInfos(loanInfos);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }
}
