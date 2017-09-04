package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.SubLoanRecord;
import com.htr.loan.domain.User;
import com.htr.loan.service.SubLoanRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subLoanRecord")
public class SubLoanRecordController {

    @Autowired
    private SubLoanRecordService subLoanRecordService;

    @RequestMapping(value = "/{currentPage}/{pageSize}", method = RequestMethod.GET)
    public Page<SubLoanRecord> findAll(@PathVariable int currentPage,
                                       @PathVariable int pageSize,
                                       @RequestParam(defaultValue = "{}") String jsonFilter) {
        Map<String, Object> filterParams = WebUtil.getParametersStartingWith(jsonFilter, Constants.SEARCH_PREFIX);
        filterParams.put("EQ_active", Constants.RECORD_EXIST);
        String sortData = "[{\"property\":\"receiptDate\",\"direction\":\"DESC\"}]";
        return subLoanRecordService.findAll(filterParams, WebUtil.buildPageRequest(currentPage, pageSize, sortData));
    }

    @RequestMapping(value = "/loanInfoID/{loanInfoID}", method = RequestMethod.GET)
    public List<SubLoanRecord> findAllByLoanInfo(@PathVariable String loanInfoID) {
        return subLoanRecordService.findAllByLoanInfo(loanInfoID);
    }

    @RequestMapping(value = "repayment", method = RequestMethod.POST)
    public SubLoanRecord repayment(@RequestBody SubLoanRecord subLoanRecord, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        subLoanRecord.setPayee(user);
        return subLoanRecordService.repayment(subLoanRecord);
    }
}
