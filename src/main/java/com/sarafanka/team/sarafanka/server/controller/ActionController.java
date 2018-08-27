package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.entity.RunningActions;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sarafanka.team.sarafanka.server.repository.ActionRepository;
import com.sarafanka.team.sarafanka.server.repository.RunningActionsRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@RestController
public class ActionController {
    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private RunningActionsRepository repo;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/actions/getall", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getAllAction(){
        return actionRepository.findAll();
    }

    @RequestMapping(value = "/actions/getcount", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getCoundOfAction(@RequestParam(value ="login",required = true,defaultValue = "1") String lgn){
        return services.getCountOfAction(lgn);
    }

    @RequestMapping(value = "/actions/getfirst", method = RequestMethod.GET)
    @ResponseBody
    public Action getFirstAction(){
        return actionRepository.findOne(Long.valueOf(1));
    }

    @RequestMapping(value = "/actions/getactionsbyid", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getActionById(@RequestParam(value ="id",required = true,defaultValue = "1") Long id,
                                      @RequestParam(value ="login",required = true,defaultValue = "1") String lgn){
        Long accountID = accRepo.findBylogin(lgn).getId();
        List<Action> list = actionRepository.findAll();
        List<Action> result = new ArrayList<Action>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Action action:list) {
            if (action.getOrganizationID().equals(id)&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                for (RunningActions ract:repo.findAll()) {
                    if (!(ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId()))){
                        result.add(action);
                        break;
                    }
                }
           }
        }
        return result;
    }

    @RequestMapping(value = "/actions/getactionforcouponbyid", method = RequestMethod.GET)
    @ResponseBody
    public Action getActionForCouponById(@RequestParam(value ="id",required = true,defaultValue = "1") Long id){

        return actionRepository.findById(id);
    }

    @RequestMapping(value = "/actions/getactionsformarketolog", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getActionsForMarketolog(@RequestParam(value ="login",required = true,defaultValue = "") String lgn){
        return services.getActionsForMarketolog(lgn);
    }

    @RequestMapping(value = "/actions/addNewAction", method = RequestMethod.GET)
    @ResponseBody
    public Integer addNewActon(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                               @RequestParam(value ="reward",required = true,defaultValue = "") String reward,
                               @RequestParam(value ="supportreward",required = true,defaultValue = "") String supportReward,
                               @RequestParam(value ="description",required = true,defaultValue = "") String description,
                               @RequestParam(value ="target",required = true,defaultValue = "") Integer target,
                               @RequestParam(value ="timestart",required = true,defaultValue = "") Long timeStart,
                               @RequestParam(value ="timeend",required = true,defaultValue = "")Long timeEnd,
                               @RequestParam(value ="est",required = true,defaultValue = "")String est){
        return services.addNewActon(login,reward,supportReward,target,description,timeStart,timeEnd,est);
    }

    @RequestMapping(value = "/actions/deletactionbyID", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteActionByID(@RequestParam(value ="actionid",required = true,defaultValue = "") Long actionID){
        return services.deleteActionByID(actionID);
    }

    @RequestMapping(value = "/actions/getCountOfActionCurrentCompany", method = RequestMethod.GET)
    @ResponseBody
    public Integer getCountOfActionCurrentCompany(@RequestParam(value ="orgid",required = true,defaultValue = "") Long orgID){
        Integer count = 0;
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Action action: actionRepository.findAll() ) {
            if (action.getOrganizationID().equals(orgID)&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                count++;
            }
        }
        return count;
    }



}