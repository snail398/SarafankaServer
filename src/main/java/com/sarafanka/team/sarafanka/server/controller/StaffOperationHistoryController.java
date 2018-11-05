package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.StaffOperationHistoryForApp;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StaffOperationHistoryController {
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/staffOperationHistory/addoperation", method = RequestMethod.GET)
    @ResponseBody
    public Integer addBarmenOperation(@RequestParam(value ="staffid",required = true,defaultValue = "1") Long staffID,
                                          @RequestParam(value ="type",required = true,defaultValue = "1") String type,
                                          @RequestParam(value ="clientloginid",required = true,defaultValue = "1") Long clientID,
                                          @RequestParam(value ="actionid",required = true,defaultValue = "1") Long actionID){
        return services.addBarmenOperation(staffID,type,clientID,actionID);
    }

    @RequestMapping(value = "/staffOperationHistory/getoperations", method = RequestMethod.GET)
    @ResponseBody
    public List<StaffOperationHistoryForApp> getStaffOperations(@RequestParam(value ="staffid",required = true,defaultValue = "1") Long staffID){
        return services.getBarmenOperations(staffID);
    }
}
