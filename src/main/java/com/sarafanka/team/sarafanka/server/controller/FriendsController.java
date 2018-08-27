package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Friends;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendsController {

    @Autowired
    private Services services = new ServicesImpl();

    //Получение списка друзей для приглашения в участии
    @RequestMapping(value = "/getfriendsforinvite", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getFriendsForInvite(@RequestParam(value ="login",required = true,defaultValue = "") String lgn,@RequestParam(value ="action",required = true,defaultValue = "") Long actionID){

        return services.getFriendsForInvite(lgn,actionID);
    }

    @RequestMapping(value = "/getfriends", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getFriends(@RequestParam(value ="login",required = true,defaultValue = "") String lgn){

        return services.getFriends(lgn);
    }

    //Создание новой заявки в друзья
    @RequestMapping(value = "/newfriends", method = RequestMethod.GET)
    @ResponseBody
    public Integer createNewFriendRequest(@RequestParam(value ="initlogin",required = true,defaultValue = "") String initLgn,@RequestParam(value ="targetlogin",required = true,defaultValue = "") String targetLgn){

        return services.createNewFriendRequest(initLgn,targetLgn);
    }

    //Принятие новой заявки в друзья
    @RequestMapping(value = "/acceptfriends", method = RequestMethod.GET)
    @ResponseBody
    public Integer acceptFriendRequest(@RequestParam(value ="accepterlogin",required = true,defaultValue = "") String accepterLgn,@RequestParam(value ="inviterlogin",required = true,defaultValue = "") String inviterLgn){

        return services.acceptFriendRequest(accepterLgn,inviterLgn);
    }

    //Получение списка заявок в друзья
    @RequestMapping(value = "/getrequestfriends", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getRequestFriends(@RequestParam(value ="login",required = true,defaultValue = "") String lgn){

        return services.getRequestFriends(lgn);
    }

    //Получение количества заявок в друзья
    @RequestMapping(value = "/friends/getrequestcount", method = RequestMethod.GET)
    @ResponseBody
    public Integer getCountOfFriendRequest(@RequestParam(value ="login",required = true,defaultValue = "") String lgn){

        return services.getCountOfFriendRequest(lgn);
    }

    //Удаление друга
    @RequestMapping(value = "/deletefriend", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteFriend(@RequestParam(value ="login",required = true,defaultValue = "") String lgn, @RequestParam(value ="friend",required = true,defaultValue = "") String friend){

        return services.deleteFriend(lgn,friend);
    }
}
