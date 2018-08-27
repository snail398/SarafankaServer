package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InviteController {

    @Autowired
    private Services services = new ServicesImpl();

        @RequestMapping(value = "/invite/getuserbyinvitetargetid", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getUsersByInviteTargetID(@RequestParam(value ="targetlogin",required = true,defaultValue = "") String targetLogin){

        return services.getUsersByInviteTargetID(targetLogin);
    }

    @RequestMapping(value = "/invite/getinvitedaction", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getInvitedAction(@RequestParam(value ="initlogin",required = true,defaultValue = "") String initLogin,@RequestParam(value ="targetlogin",required = true,defaultValue = "") String targetLogin){

        return services.getInvitedAction(initLogin,targetLogin);
    }

    @RequestMapping(value = "/invite/addnewinvite", method = RequestMethod.POST)
    @ResponseBody
    public Integer addNewInvite(@RequestParam(value ="initlogin",required = true,defaultValue = "") String initLogin,@RequestParam(value ="action",required = true,defaultValue = "") Long actionID, @RequestBody Long[] targetLogins){

            return services.addNewInvite(initLogin,actionID,targetLogins);
    }

    @RequestMapping(value = "/test/post", method = RequestMethod.POST)
    @ResponseBody
    public Integer testPost(@RequestParam(value ="initlogin",required = true,defaultValue = "") String initLogin, @RequestBody String[] input){
            String[] test = input;
            return null;
        //  return services.getInvitedAction(initLogin,targetLogin);
    }
}
