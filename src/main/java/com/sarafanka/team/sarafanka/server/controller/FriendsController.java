package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Friends;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sarafanka.team.sarafanka.server.repository.FriendsRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendsController {

    @Autowired
    private Services services = new ServicesImpl();
    @Autowired
    private FriendsRepository friendsRepository;
    @Autowired
    private AccountRepository accountRepository;

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

    //Запрос статуса человека. 0 - нет связей (можно добавить), 1 - друзья (можно удалить), -1 - ожидание ответа на запрос пользователя(заглушка) , -2 - ожидание ответа на запрос друга(можно принять)
    @RequestMapping(value = "/friends/getfriendstatus", method = RequestMethod.GET)
    @ResponseBody
    public Integer getFriendStatus(@RequestParam(value ="login",required = true,defaultValue = "") String lgn, @RequestParam(value ="friend",required = true,defaultValue = "") String friend){
        Integer response = 0;
        Long userID = accountRepository.findBylogin(lgn).getId();
        Long friendID = accountRepository.findBylogin(friend).getId();
        for (Friends fr: friendsRepository.findAll()) {
            if (fr.getUser1_id().equals(userID) && fr.getUser2_id().equals(friendID)){
                switch (fr.getStatus()){
                    case 0:
                        response = -1;
                        break;
                    case 1:
                        response = 1;
                        break;
                }
            }
            if (fr.getUser2_id().equals(userID) && fr.getUser1_id().equals(friendID)){
                switch (fr.getStatus()){
                    case 0:
                        response = -2;
                        break;
                    case 1:
                        response = 1;
                        break;
                }
            }

        }
        return response;
    }
}
