package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.BarmensOperationHistoryForApp;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BarmenOperationHistoryController {
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/barmenOperationHistory/addoperation", method = RequestMethod.GET)
    @ResponseBody
    public Integer addBarmenOperation(@RequestParam(value ="login",required = true,defaultValue = "1") String lgn,
                                          @RequestParam(value ="type",required = true,defaultValue = "1") String type,
                                          @RequestParam(value ="clientlogin",required = true,defaultValue = "1") String clientLgn,
                                          @RequestParam(value ="actionid",required = true,defaultValue = "1") Long actionID){
        return services.addBarmenOperation(lgn,type,clientLgn,actionID);
    }

    @RequestMapping(value = "/barmenOperationHistory/getoperations", method = RequestMethod.GET)
    @ResponseBody
    public List<BarmensOperationHistoryForApp> getBarmenOperations(@RequestParam(value ="login",required = true,defaultValue = "1") String lgn){
        return services.getBarmenOperations(lgn);
    }
}
