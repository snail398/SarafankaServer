package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.entity.RunningActions;
import com.sarafanka.team.sarafanka.server.repository.RunningActionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class RunningActionsController {

    @Autowired
    private RunningActionsRepository repo;
    @RequestMapping(value = "/runningactions/add?login={lgn}&action={title}", method = RequestMethod.POST)
    @ResponseBody
    public void addNewRunningActions(@PathVariable("lgn") String lgn, @PathVariable("title") String actionTitle){
        RunningActions runAct = new RunningActions();
       // runAct.setId(repo.count());
        runAct.setAccountLogin(lgn);
        runAct.setActionTitle(actionTitle);
        repo.saveAndFlush(runAct);
    }
}
