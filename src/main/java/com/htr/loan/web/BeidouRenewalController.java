package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.BeidouRenewal;
import com.htr.loan.service.BeidouRenewalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/beidouRenewal")
public class BeidouRenewalController {

    @Autowired
    private BeidouRenewalService beidouRenewalService;

    @RequestMapping(value = "/{currentPage}/{pageSize}", method = RequestMethod.GET)
    public Page<BeidouRenewal> findAll(@PathVariable int currentPage,
                                       @PathVariable int pageSize,
                                       @RequestParam(defaultValue = "{}") String jsonFilter) {
        Map<String, Object> filterParams = WebUtil.getParametersStartingWith(jsonFilter, Constants.SEARCH_PREFIX);
        filterParams.put("EQ_active", Constants.RECORD_EXIST);
        String sortData = "[{\"property\":\"renewalDate\",\"direction\":\"DESC\"}]";
        return beidouRenewalService.findAll(filterParams, WebUtil.buildPageRequest(currentPage, pageSize, sortData));
    }

    @RequestMapping(value = "/beidouRecordID/{beidouRecordID}", method = RequestMethod.GET)
    public List<BeidouRenewal> findAllByLoanInfo(@PathVariable String beidouRecordID) {
        return beidouRenewalService.findAllByBeidouRecord(beidouRecordID);
    }

    @RequestMapping(value = "renewal", method = RequestMethod.POST)
    public BeidouRenewal renewal(@RequestBody BeidouRenewal beidouRenewal) {
        return beidouRenewalService.saveBeidouRenewal(beidouRenewal);
    }
}
