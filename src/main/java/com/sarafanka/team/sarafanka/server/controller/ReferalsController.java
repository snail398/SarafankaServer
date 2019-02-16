package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReferalsController {
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/referals/addnew", method = RequestMethod.POST)
    @ResponseBody
    public Integer addNewReferal(@RequestParam(value ="ractid",required = true,defaultValue = "1") Long ractID,@RequestBody Account account){
        return services.addNewReferal(ractID,account);
    }

    @RequestMapping(value = "/referals/checkRef", method = RequestMethod.GET)
    @ResponseBody
    public String checkReferal(@RequestParam(value ="ref",required = true,defaultValue = "1") String ref,@RequestParam(value ="staffid",required = true,defaultValue = "1") Long staffID){
        return services.checkReferal(ref,staffID);
    }
}
