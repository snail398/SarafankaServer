package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.ImageHandler;
import com.sarafanka.team.sarafanka.server.entity.*;
import com.sarafanka.team.sarafanka.server.repository.*;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class LoginController {


    @Autowired
    private AccountRepository accRep;
    @Autowired
    private FriendsRepository friendRep;
    @Autowired
    private BarmenRepository barmenRep;
    @Autowired
    private BarmenWorkingPlaceRepository bwpRep;
    @Autowired
    private EstablishmentRepository establishmentRepository;
    @Autowired
    private CookieAndSessionRepository cookieAndSessionRepository;
    @Autowired
    private Services services = new ServicesImpl();
    @Autowired
    private MarketologRepository marketologRepo;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Integer loginQuery(  HttpServletResponse response, HttpServletRequest request){
        cookieAndSessionRepository.deleteAllByCookie(request.getHeader("Cookie"));
        response.setHeader("Set-Cookie","");
        return 1;
    }
    //
    // Поиск аккаунта в таблице по логину и паролю. Возвращает true, если запрашиваемая пара будет найдена.
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Account> loginQuery(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,
                                              @RequestParam (value ="pass",required = true,defaultValue = "") String pass,
                                              HttpServletResponse response,HttpServletRequest request){
        String userCookie = request.getHeader("Cookie");
        if ( userCookie==null || userCookie.equals("defaultCookie")|| userCookie.equals("")){
        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim().toUpperCase();
        String targetPass = pass.trim().toUpperCase();
        Account result = new Account();
        result.setAccountType("none");
        //Получаем список всех аккаунтов
        List<Account> list = accRep.findAll();



        //Поиск аккаунта в списке
        for (Account acc:list) {
            if ((acc.getLogin().toUpperCase().equals(targetLogin)|| acc.getPhoneNumber().equals(targetLogin)) && acc.getPassword().toUpperCase().equals(targetPass)){
                result = acc;
                cookieAndSessionRepository.deleteAllByAccountID(acc.getId());
                CookieAndSession cookie = new CookieAndSession();
                cookie.setAccountID(acc.getId());
                cookie.setCookie(ImageHandler.get_SHA_512(String.valueOf(acc.getId()),"sarafankacookie"));
                cookieAndSessionRepository.saveAndFlush(cookie);
                response.setHeader("Set-Cookie",cookie.getCookie());
                break;
            }
        }
        return  new ResponseEntity<Account>(result,HttpStatus.OK);
        }
        else {

            if (cookieAndSessionRepository.findAllByCookie(userCookie).size()>1){
                cookieAndSessionRepository.deleteAllByCookie(userCookie);
                return  new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
            }
            if (cookieAndSessionRepository.findAllByCookie(userCookie).size()==0){
                return  new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
            }
            return  new ResponseEntity<Account>(accRep.findByid(cookieAndSessionRepository.findByCookie(userCookie).getAccountID()),HttpStatus.OK);
        }
    }

    // Поиск аккаунта в таблице по логину и паролю. Возвращает true, если запрашиваемая пара будет найдена.
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    @ResponseBody
    public Integer registrationQuery(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,
                                     @RequestParam (value ="pass",required = true,defaultValue = "") String pass,
                                     @RequestParam (value ="firstname",required = true,defaultValue = "") String firstname,
                                     @RequestParam (value ="secondname",required = true,defaultValue = "") String secondname){
        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim();
        String targetPass = pass.trim();
        Integer responseCode = 0;
        Boolean duplicate =false;

        //Получаем список всех аккаунтов
        List<Account> list = accRep.findAll();
        //Поиск аккаунта в списке (Проверка на дублирование логинов)
        for (Account acc:list) {
            if (acc.getLogin().equals(targetLogin)){
                duplicate = true;
                break;
            }
        }
        //Если нет такого логина, созадаем новый аккаунт
        if(!duplicate){
            Calendar c = Calendar.getInstance();
            Long date = c.getTimeInMillis();
            Account newAcc = new Account();
            newAcc.setLogin(targetLogin);
            newAcc.setPassword(targetPass);
            newAcc.setAccountType("user");
            newAcc.setFirstName(firstname);
            newAcc.setSecondName(secondname);
            newAcc.setPathToAvatar("noavatar");
            newAcc.setCreatingDate(date);
            newAcc.setPhoneNumber("0");
            accRep.saveAndFlush(newAcc);
            responseCode =1;
        }
        else{
            responseCode = -1;
        }
        return  responseCode;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public Account registrationQuery(@RequestBody Account newAcc){
        Boolean duplicateEmail =false;
        Boolean duplicatePhone =false;
        Account responseAccount = new Account();
        responseAccount.setAccountType("none");
        //Получаем список всех аккаунтов
        List<Account> list = accRep.findAll();
        //Поиск аккаунта в списке (Проверка на дублирование логинов)
        for (Account acc:list) {
            if (acc.getLogin().equals(newAcc.getLogin())){
                duplicateEmail = true;
                break;
            }
        }
        for (Account acc:list) {
            if (acc.getPhoneNumber().equals(newAcc.getPhoneNumber())){
                duplicatePhone = true;
                break;
            }
        }
        //Если нет такого логина, созадаем новый аккаунт
        if(!duplicateEmail && !duplicatePhone){
            accRep.saveAndFlush(newAcc);
            responseAccount = accRep.findByPhoneNumber(newAcc.getPhoneNumber());
        }
        return  responseAccount;
    }

    @RequestMapping(value = "/registration/barmen", method = RequestMethod.GET)
    @ResponseBody
    public Integer registrationBarmenQuery(@RequestParam (value ="creatorid",required = true,defaultValue = "") Long creatorID,
            @RequestParam (value ="login",required = true,defaultValue = "") String lgn,
                                     @RequestParam (value ="pass",required = true,defaultValue = "") String pass,
                                     @RequestParam (value ="firstname",required = true,defaultValue = "") String firstname,
                                     @RequestParam (value ="secondname",required = true,defaultValue = "") String secondname,
                                           @RequestParam (value ="estadress",required = true,defaultValue = "") String estAdress){
        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim();
        String targetPass = pass.trim();
        Integer responseCode = 0;
        Boolean duplicate =false;

        //Получаем список всех аккаунтов
        List<Account> list = accRep.findAll();
        //Поиск аккаунта в списке (Проверка на дублирование логинов)
        for (Account acc:list) {
            if (acc.getLogin().equals(targetLogin)){
                duplicate = true;
                break;
            }
        }
        //Если нет такого логина, созадаем новый аккаунт
        if(!duplicate){
            Calendar c = Calendar.getInstance();
            Long date = c.getTimeInMillis();
            Account newAcc = new Account();
            newAcc.setLogin(targetLogin);
            newAcc.setPassword(targetPass);
            newAcc.setAccountType("barmen");
            newAcc.setFirstName(firstname);
            newAcc.setSecondName(secondname);
            newAcc.setPathToAvatar("noavatar");
            newAcc.setCreatingDate(date);
            newAcc.setPhoneNumber("0");
            accRep.saveAndFlush(newAcc);
            Barmen barmen = new Barmen();
            barmen.setAccountID(accRep.findBylogin(targetLogin).getId());
            barmen.setCompanyID(marketologRepo.findByAccountID(creatorID).getCompanyID());
            barmenRep.saveAndFlush(barmen);
            if (!estAdress.equals("Все заведения")) {
                BarmenWorkingPlace bwp = new BarmenWorkingPlace();
                bwp.setEstablishmentID(establishmentRepository.findByEstName(estAdress).getId());
                bwp.setbarmenID(barmenRep.findByAccountID(accRep.findBylogin(targetLogin).getId()).getId());
                bwpRep.saveAndFlush(bwp);
            }
            else {
                for (Establishment est: establishmentRepository.findByCompanyID(marketologRepo.findByAccountID(creatorID).getCompanyID())) {
                    BarmenWorkingPlace bwp = new BarmenWorkingPlace();
                    bwp.setEstablishmentID(est.getId());
                    bwp.setbarmenID(barmenRep.findByAccountID(accRep.findBylogin(targetLogin).getId()).getId());
                    bwpRep.saveAndFlush(bwp);
                }
            }

            responseCode =1;
        }
        else{
            responseCode = -1;
        }
        return  responseCode;
    }

    @RequestMapping(value = "/registration/mainmarketolog", method = RequestMethod.POST)
    @ResponseBody
    public Integer registrationMainMarketologQuery(@RequestParam (value ="login",required = true,defaultValue = "") String lgn,
                                           @RequestParam (value ="pass",required = true,defaultValue = "") String pass,
                                           @RequestParam (value ="firstname",required = true,defaultValue = "") String firstname,
                                           @RequestParam (value ="secondname",required = true,defaultValue = "") String secondname,
                                                   @RequestParam(value ="companyname",required = true,defaultValue = "") String companyName,
                                                   @RequestParam(value ="companycategory",required = true,defaultValue = "") String companyCategory,
                                                   @RequestParam(value ="companytype",required = true,defaultValue = "") String companyType,
                                                   @RequestParam(value ="companydescription",required = true,defaultValue = "") String companyDescription,
                                                   @RequestParam(value ="companyadress",required = true,defaultValue = "") String companyAdress,
                                                   @RequestParam(value ="companyphone",required = true,defaultValue = "") String companyPhone,
                                                   @RequestParam(value ="companysite",required = true,defaultValue = "") String companySite, @RequestBody Establishment newEst ){
        return services.registrationMainMarketolog(lgn,pass,firstname,secondname,companyName,companyCategory,companyType,companyDescription,companyAdress,companyPhone,companySite,newEst);
    }

    @RequestMapping(value = "/registration/marketolog", method = RequestMethod.GET)
    @ResponseBody
    public Integer registrationMarketologQuery(@RequestParam (value ="creatorid",required = true,defaultValue = "") Long creatorID,
            @RequestParam (value ="login",required = true,defaultValue = "") String lgn,
                                     @RequestParam (value ="pass",required = true,defaultValue = "") String pass,
                                     @RequestParam (value ="firstname",required = true,defaultValue = "") String firstname,
                                     @RequestParam (value ="secondname",required = true,defaultValue = "") String secondname,
                                               @RequestParam (value ="estadress",required = true,defaultValue = "") String estAdress){
        return services.registrationMarketolog(creatorID,lgn,pass,firstname,secondname,estAdress);
    }

    @RequestMapping(value = "/searchlogin", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> SearchLoginQuery(@RequestParam (value ="searchedstring",required = true,defaultValue = "") String searchedString,
                                          @RequestParam (value ="login",required = true,defaultValue = "") String login){
        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String searched = searchedString.trim();
        Long accID = accRep.findBylogin(login).getId();

        List<Account> result = new ArrayList<Account>();
        //Получаем список всех аккаунтов
        List<Account> list = accRep.findAll();
        //Поиск аккаунта в списке
        for (Account acc:list) {
            if (!acc.getLogin().equals(login) // Нельзя найти себя
                    && acc.getAccountType().equals("user")  // Нельзя найти не юзера
                    && (acc.getLogin().toUpperCase().equals(searched) || acc.getFirstName().toUpperCase().equals(searched)|| acc.getSecondName().toUpperCase().equals(searched) || (acc.getFirstName().toUpperCase()+ " "+acc.getSecondName().toUpperCase()).equals(searched)))
            {
                Boolean flag = true;

                //Нельзя найти друга
                for (Friends friend: friendRep.findAll()) {
                    if (friend.getUser1_id().equals(accID) && friend.getUser2_id().equals(acc.getId()) ||friend.getUser1_id().equals(acc.getId()) && friend.getUser2_id().equals(accID)){
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    result.add(acc);

                }
            }
        }
        return  result;
    }

    // Запрос на изменение данных аккаунта
    @RequestMapping(value = "/account/changeinfo", method = RequestMethod.GET)
    @ResponseBody
    public Integer changeInfoQuery(@RequestParam (value ="oldemail",required = true,defaultValue = "") String oldemail,
                                     @RequestParam (value ="email",required = true,defaultValue = "") String email,
                                     @RequestParam (value ="firstname",required = true,defaultValue = "") String firstname,
                                     @RequestParam (value ="secondname",required = true,defaultValue = "") String secondname){

        return  services.changeInfo(oldemail,email,firstname,secondname);
    }

    // Запрос на удаление аккаунта
    @RequestMapping(value = "/account/deleteaccount", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteAccountQuery(@RequestParam (value ="email",required = true,defaultValue = "") String email){

        return  services.deleteAccount(email);
    }

    // Запрос на изменение пароля
    @RequestMapping(value = "/account/changePass", method = RequestMethod.GET)
    @ResponseBody
    public Integer changePassQuery(@RequestParam (value ="email",required = true,defaultValue = "") String email,
                                   @RequestParam (value ="oldpass",required = true,defaultValue = "") String oldpass,
                                   @RequestParam (value ="newpass",required = true,defaultValue = "") String newpass){
       if( !accRep.findBylogin(email).getPassword().equals(oldpass)){
           return  -1;
       }
       else {
           accRep.changePass(email, newpass);
           return 1;
       }
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
