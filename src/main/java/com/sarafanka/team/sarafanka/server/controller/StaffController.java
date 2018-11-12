package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Establishment;
import com.sarafanka.team.sarafanka.server.entity.StaffForApp;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StaffController {
    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/staff/getmarketologs", method = RequestMethod.GET)
    @ResponseBody
    public List<StaffForApp> getMarketologs(@RequestParam(value ="mainmarketologaccountid",required = true,defaultValue = "1") Long mainMarketologAccountID){
        return services.getMarketologs(mainMarketologAccountID);
    }

    @RequestMapping(value = "/staff/getbarmens", method = RequestMethod.GET)
    @ResponseBody
    public List<StaffForApp> getBarmens(@RequestParam(value ="mainmarketologaccountid",required = true,defaultValue = "1") Long mainMarketologAccountID){
        return services.getBarmens(mainMarketologAccountID);
    }

    @RequestMapping(value = "/staff/deletestaff", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteStaff(@RequestParam(value ="accID",required = true,defaultValue = "1") Long accID){
        return services.deleteStaff(accID);
    }

    @RequestMapping(value = "/staff/getstaff", method = RequestMethod.GET)
    @ResponseBody
    public Account getStaff(@RequestParam(value ="accID",required = true,defaultValue = "1") Long accID){
        return services.getStaff(accID);
    }

    @RequestMapping(value = "/staff/getaccountsest", method = RequestMethod.GET)
    @ResponseBody
    public Establishment getAccountsEst(@RequestParam(value ="accID",required = true,defaultValue = "1") Long accID){
        return services.getAccountsEst(accID);
    }

    @RequestMapping(value = "/staff/changestaffinfo", method = RequestMethod.POST)
    @ResponseBody
    public Integer changeStaffInfo(@RequestParam(value ="creatorid",required = true,defaultValue = "1") Long accID,
                                         @RequestParam(value ="estname",required = true,defaultValue = "")String estname,
                                         @RequestBody Account account){
        return services.changeStaffInfo(accID,estname,account);
    }

}
