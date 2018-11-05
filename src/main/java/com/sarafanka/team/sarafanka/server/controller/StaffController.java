package com.sarafanka.team.sarafanka.server.controller;

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
    public List<StaffForApp> getMarketologs(@RequestParam(value ="mainmarketologlogin",required = true,defaultValue = "1") String mainMarketologLogin){
        return services.getMarketologs(mainMarketologLogin);
    }

    @RequestMapping(value = "/staff/getbarmens", method = RequestMethod.GET)
    @ResponseBody
    public List<StaffForApp> getBarmens(@RequestParam(value ="mainmarketologlogin",required = true,defaultValue = "1") String mainMarketologLogin){
        return services.getBarmens(mainMarketologLogin);
    }

    @RequestMapping(value = "/staff/deletestaff", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteStaff(@RequestParam(value ="accID",required = true,defaultValue = "1") Long accID){
        return services.deleteStaff(accID);
    }

}
