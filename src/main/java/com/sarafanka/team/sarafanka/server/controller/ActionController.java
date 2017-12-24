package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ActionController {
    @Autowired
    private ActionRepository actionRepository;

    @RequestMapping(value = "/actions/getall", method = RequestMethod.GET)
    @ResponseBody
    public List<Action> getAllAction(){
        return actionRepository.findAll();
    }

    @RequestMapping(value = "/actions/getfirst", method = RequestMethod.GET)
    @ResponseBody
    public Action getFirstAction(){
        return actionRepository.findOne(Long.valueOf(1));
    }
}
