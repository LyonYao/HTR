package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/systemLog")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @RequestMapping(value = "/{currentPage}/{pageSize}", method = RequestMethod.GET)
    public Page<SystemLog> findAll(@PathVariable int currentPage,
                                @PathVariable int pageSize,
                                   @RequestParam(defaultValue = "[{\"property\":\"createdDate\",\"direction\":\"DESC\"}]") String sortData,
                                    @RequestParam(defaultValue = "{}") String jsonFilter){
        Map<String, Object> filterParams = WebUtil.getParametersStartingWith(jsonFilter, Constants.SEARCH_PREFIX);
        filterParams.put("EQ_active", Constants.RECORD_EXIST);
        return systemLogService.findAll(filterParams, WebUtil.buildPageRequest(currentPage, pageSize, sortData));
    }
}
