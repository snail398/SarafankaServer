package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {


    @Autowired
    private AccountRepository accRep;

    // Поиск аккаунта в таблице по логину и паролю. Возвращает true, если запрашиваемая пара будет найдена.
    @RequestMapping(value = "/login/login={lgn}&password={pass}", method = RequestMethod.GET)
    @ResponseBody
    public String loginQuery(@PathVariable("lgn") String lgn, @PathVariable("pass") String pass){
        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim();
        String targetPass = pass.trim();

        String result = "none";

        //Получаем список всех аккаунтов
        List<Account> list = accRep.findAll();
        //Поиск аккаунта в списке
        for (Account acc:list) {
            if (acc.getLogin().equals(targetLogin) && acc.getPassword().equals(targetPass)){
                result = acc.getAccountType();
            }
        }
        return  result;

    }

    private Account createAccount() {
        Account acc = new Account();
        acc.setId(1);
        acc.setLogin("admin");
        acc.setPassword("1234");
        acc.setAccountType("god");
        return acc;
    }

}
