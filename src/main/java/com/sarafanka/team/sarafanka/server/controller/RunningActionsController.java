package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.repository.RunningActionsRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @RequestMapping(value = "/runningactions/newract", method = RequestMethod.POST)
    @ResponseBody
    public Integer addNewRunningActions(@RequestBody Account acc, @RequestParam (value ="action",required = true,defaultValue = "") Long id,@RequestParam (value ="messagetype",required = true,defaultValue = "") String messageType) throws IOException {

        return services.addNewRunningActions(acc,id,messageType);
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

    //Получение списка названий акций,вкоторых участвует данный логин
    @RequestMapping(value = "/runningactions/changeprogressforsocial", method = RequestMethod.GET)
    @ResponseBody
    public Integer ChangeProgressForSocial(@RequestParam (value ="userid",required = true,defaultValue = "") Long userID,
                                            @RequestParam (value ="actionid",required = true,defaultValue = "") Long actionID,
                                           @RequestParam (value ="staffid",required = true,defaultValue = "") Long barmenID){
        return services.ChangeProgressForSocial(userID,actionID,barmenID);
    }

    //Получение количества людей для завершения акции
    @RequestMapping(value = "/runningactions/getcountpeopletoend", method = RequestMethod.GET)
    @ResponseBody
    public Integer getCountPeopleToEnd(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,@RequestParam (value ="actionid",required = true,defaultValue = "") Long actionID){
        return services.getCountPeopleToEnd(lgn,actionID);
    }


    //Удаление запущенной акции
    @RequestMapping(value = "/runningactions/delete", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteRAact(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,@RequestParam (value ="actionid",required = true,defaultValue = "") Long actionID){
        return services.deleteRAact(lgn,actionID);
    }
}
