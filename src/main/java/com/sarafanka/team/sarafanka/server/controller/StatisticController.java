package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.ActionStatistic;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StatisticController {
    @Autowired
    private Services services = new ServicesImpl();


    @RequestMapping(value = "/statistic/getstatbymarketid", method = RequestMethod.GET)
    @ResponseBody
    public List<ActionStatistic> getStatisticByMarketolog(@RequestParam(value ="marketologid",required = true,defaultValue = "1") Long marketologID){
        return services.getStatisticByMarketolog(marketologID);
    }
}
