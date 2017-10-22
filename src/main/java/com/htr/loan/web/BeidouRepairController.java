package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.BeidouRepair;
import com.htr.loan.service.BeidouRepairService;
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
@RequestMapping("/beidouRepair")
public class BeidouRepairController {

    @Autowired
    private BeidouRepairService beidouRepairService;

    @RequestMapping(value = "/{currentPage}/{pageSize}", method = RequestMethod.GET)
    public Page<BeidouRepair> findAll(@PathVariable int currentPage,
                                       @PathVariable int pageSize,
                                       @RequestParam(defaultValue = "{}") String jsonFilter) {
        Map<String, Object> filterParams = WebUtil.getParametersStartingWith(jsonFilter, Constants.SEARCH_PREFIX);
        filterParams.put("EQ_active", Constants.RECORD_EXIST);
        String sortData = "[{\"property\":\"repairDate\",\"direction\":\"DESC\"}]";
        return beidouRepairService.findAll(filterParams, WebUtil.buildPageRequest(currentPage, pageSize, sortData));
    }

    @RequestMapping(value = "/beidouRecordID/{beidouRecordID}", method = RequestMethod.GET)
    public List<BeidouRepair> findAllByLoanInfo(@PathVariable String beidouRecordID) {
        return beidouRepairService.findAllByBeidouRecord(beidouRecordID);
    }

    @RequestMapping(value = "renewal", method = RequestMethod.POST)
    public BeidouRepair renewal(@RequestBody BeidouRepair beidouRepair) {
        return beidouRepairService.saveBeidouRepair(beidouRepair);
    }
}
