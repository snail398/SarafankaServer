package com.sarafanka.team.sarafanka.server.controller;

import com.google.zxing.WriterException;
import com.sarafanka.team.sarafanka.server.entity.*;
import com.sarafanka.team.sarafanka.server.repository.*;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
    private EstablishmentRepository establishmentRepository;
    @Autowired
    private ActionAccessRepository actionAccessRepository;
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/actions/getall", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getAllAction(){
        return actionRepository.findAll();
    }

    @RequestMapping(value = "/actions/getcount", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getCoundOfAction(@RequestParam(value ="accountid",required = true,defaultValue = "1") Long accountID){
        return services.getCountOfAction(accountID);
    }

    @RequestMapping(value = "/actions/getfirst", method = RequestMethod.GET)
    @ResponseBody
    public Action getFirstAction(){
        return actionRepository.findOne(Long.valueOf(1));
    }

    @RequestMapping(value = "/actions/getactionsbyid", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getActionById(@RequestParam(value ="id",required = true,defaultValue = "1") Long id,
                                      @RequestParam(value ="accountid",required = true,defaultValue = "1") Long accountID){
        List<Action> list = actionRepository.findAll();
        List<Action> result = new ArrayList<Action>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Action action:list) {
            if (action.getOrganizationID().equals(id)&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                Boolean flag = true;
                for (RunningActions ract:repo.findAll()) {
                    if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId()) &&!ract.getComplited().equals(-1)) {
                        flag = false;
                        break;
                    }
                }
                    if (flag)
                        result.add(action);
           }
        }
        return result;
    }

    @RequestMapping(value = "/actions/getactionsforuser", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<Long,List<Action>> getActionsForUser(@RequestParam(value ="accountid",required = true,defaultValue = "1") Long accountID) {
        HashMap<Long,List<Action>> result = new HashMap<>();
        List<Company> companiesList = services.getCompanyWithAction(accountID);
        List<Action> list = actionRepository.findAll();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Company company: companiesList) {
            List<Action> tempResult = new ArrayList<Action>();
            for (Action action:list) {
                if (action.getOrganizationID().equals(company.getId())&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                    Boolean flag = true;
                    for (RunningActions ract:repo.findAll()) {
                        if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId()) &&!ract.getComplited().equals(-1)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag)
                        tempResult.add(action);
                }
            }
            result.put(company.getId(),tempResult);
        }
        return result;
    }
    @RequestMapping(value = "/actions/getactionforcouponbyid", method = RequestMethod.GET)
    @ResponseBody
    public Action getActionForCouponById(@RequestParam(value ="id",required = true,defaultValue = "1") Long id){
        return actionRepository.findById(id);
    }

    @RequestMapping(value = "/actions/getactionsest", method = RequestMethod.GET)
    @ResponseBody
    public Establishment getActionsEst(@RequestParam(value ="actionid",required = true,defaultValue = "1") Long actionID){
        Establishment establishment = new Establishment();
        establishment.setEstName("Все заведения");
        for (ActionAccess actionAccess: actionAccessRepository.findAll()) {
            if (actionAccess.getActionID().equals(actionID)){
                establishment = establishmentRepository.findByid(actionAccess.getEstablishmentID());
            }
        }
        return establishment;
    }



    @RequestMapping(value = "/actions/getactionsformarketolog", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getActionsForMarketolog(@RequestParam(value ="login",required = true,defaultValue = "") String lgn){
        return services.getActionsForMarketolog(lgn);
    }

    @RequestMapping(value = "/actions/getactionsforstaff", method = RequestMethod.POST)
    @ResponseBody
    public List<Action> getActionsForStaff(@RequestBody Account account){
        return services.getActionsForStaff(account);
    }

    @RequestMapping(value = "/actions/getactionsbyarr", method = RequestMethod.POST)
    @ResponseBody
    public List<Action> getActionsByArr(@RequestBody Long[] actionIDs){
        List<Action> actions = new ArrayList<>();
        for (int i = 0; i < actionIDs.length; i++) {
            actions.add(actionRepository.findById(actionIDs[i]));
        }
        return actions;
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

    @RequestMapping(value = "/actions/addNewAction", method = RequestMethod.POST)
    @ResponseBody
    public Integer addNewActon2(@RequestBody Action action,@RequestParam(value ="creatorid",required = true,defaultValue = "") Long creatorID,
                               @RequestParam(value ="est",required = true,defaultValue = "")String est) throws IOException, WriterException {
        return services.addNewActon2(action,creatorID,est);
    }

    @RequestMapping(value = "/actions/deletactionbyID", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteActionByID(@RequestParam(value ="actionid",required = true,defaultValue = "") Long actionID){
        return services.deleteActionByID(actionID);
    }

    @RequestMapping(value = "/actions/changeaction", method = RequestMethod.POST)
    @ResponseBody
    public Integer changeAction(@RequestBody Action action,@RequestParam(value ="est",required = true,defaultValue = "")String est){
        actionRepository.changeAction(action.getId(),action.getReward(),action.getSupportReward(),action.getTarget(),action.getDescription(),action.getTimeStart(),action.getTimeEnd(),action.getCondition());
        for (ActionAccess actionAccess: actionAccessRepository.findAll()) {
            if (actionAccess.getActionID().equals(action.getId())){
                actionAccessRepository.delete(actionAccess);
            }
        }
        if (!est.equals("Все заведения")){
            ActionAccess actionAccess = new ActionAccess();
            actionAccess.setActionID(action.getId());
            actionAccess.setEstablishmentID(establishmentRepository.findByEstName(est).getId());
            actionAccessRepository.saveAndFlush(actionAccess);
        }
        return 1;
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
