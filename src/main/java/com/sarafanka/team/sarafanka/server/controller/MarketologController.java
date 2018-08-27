package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Coupon;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarketologController {
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/marketolog/getmarketologrole", method = RequestMethod.GET)
    @ResponseBody
    public Integer getMarketologRole(@RequestParam(value ="login",required = true,defaultValue = "") String lgn)
    {
        return services.getMarketologRole(lgn);
    }


}
