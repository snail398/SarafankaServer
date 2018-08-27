package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.repository.RunningActionsRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Transactional
@RestController
public class RunningActionsController {

    @Autowired
    private RunningActionsRepository repo;
    @Autowired
    private Services services = new ServicesImpl();
    //Добавление в таблицу активных акций по логину и названию акции
    @RequestMapping(value = "/runningactions/new", method = RequestMethod.GET)
    @ResponseBody
    public Integer addNewRunningActions(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,@RequestParam (value ="action",required = true,defaultValue = "") Long id){

        return services.addNewRunningActions(lgn,id);
    }
    //Получение списка названий акций,вкоторых участвует данный логин
    @RequestMapping(value = "/getrunningactionsbylogin", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getRunningActionsByLogin(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,@RequestParam (value ="orgid",required = true,defaultValue = "") Long orgId, @RequestParam (value ="ifcomplited",required = true,defaultValue = "") Integer ifComplited ){
        return services.getRunningActionsByLoginAndOrgId(lgn, orgId, ifComplited);
    }

    //Получение списка названий акций,вкоторых участвует данный логин
    @RequestMapping(value = "/getfriendsrunningactions", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getFriendsRunningActions(@RequestParam (value ="login",required = true,defaultValue = "") String lgn){
        return services.getFriendsRunningActions(lgn);
    }

    //Получение списка названий акций,вкоторых участвует данный логин
    @RequestMapping(value = "/runningactions/changeprogress", method = RequestMethod.GET)
    @ResponseBody
    public Integer ChangeProgress(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,
                                  @RequestParam (value ="action",required = true,defaultValue = "") Long id,
                                  @RequestParam (value ="userlogin",required = true,defaultValue = "") String userLogin,
                                  @RequestParam (value ="barmenlogin",required = true,defaultValue = "") String barmenLogin){
           return services.ChangeProgress(lgn,id, userLogin, barmenLogin);
    }

    //Получение количества людей для завершения акции
    @RequestMapping(value = "/runningactions/getcountpeopletoend", method = RequestMethod.GET)
    @ResponseBody
    public Integer getCountPeopleToEnd(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,@RequestParam (value ="actionid",required = true,defaultValue = "") Long actionID){
        return services.getCountPeopleToEnd(lgn,actionID);
    }

}
