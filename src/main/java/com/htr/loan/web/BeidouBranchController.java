package com.htr.loan.web;

import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.BeidouBranch;
import com.htr.loan.service.BeidouBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/beidouBranch")
public class BeidouBranchController {

    @Autowired
    private BeidouBranchService beidouBranchService;

    @RequestMapping(value = "/{active}", method = RequestMethod.GET)
    public List<BeidouBranch> findAllBeidouBranch(@PathVariable int active){
        if (active == 0){
            return beidouBranchService.findAllBeidouBranch();
        } else return beidouBranchService.findAllByActiveTrue();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public BeidouBranch saveBeidouBranch(@RequestBody BeidouBranch beidouBranch){
        return beidouBranchService.saveBeidouBranch(beidouBranch);
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> stopBeidouBranch(@RequestBody List<BeidouBranch> beidouBranchs){
        boolean isDeleted = beidouBranchService.activeOrStopBeidouBranchs(beidouBranchs, false);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

    @RequestMapping(value = "active", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> activeBeidouBranch(@RequestBody List<BeidouBranch> beidouBranchs){
        boolean isDeleted = beidouBranchService.activeOrStopBeidouBranchs(beidouBranchs, true);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }
}
