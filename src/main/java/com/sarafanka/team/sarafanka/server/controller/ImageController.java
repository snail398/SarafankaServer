package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.Constants;
import com.sarafanka.team.sarafanka.server.ImageHandler;
import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Company;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sarafanka.team.sarafanka.server.repository.CompaniesRepository;
import com.sarafanka.team.sarafanka.server.repository.MarketologRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RestController
public class ImageController {

    @Autowired
    private AccountRepository accRep;

    @Autowired
    private CompaniesRepository compRep;
    @Autowired
    private MarketologRepository marketRep;
    @Autowired
    private Services services = new ServicesImpl();


    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    @RequestMapping(value="/uploadavatar", method=RequestMethod.POST)
    public @ResponseBody Integer handleFileUpload(@RequestParam(value ="login",required = true,defaultValue = "") String login,@RequestBody MultipartFile file){
        String name = "";
        String pathToAvatars = "";
        String pathname ="sosiska";
        String pathToDB ="";
        int responseCode;
        String response ="";
        String strForMD5 ="";
        Integer userCode= 0;
        switch (accRep.findBylogin(login).getAccountType()){
            case "user":
                userCode =1;
                strForMD5 = login+"-avatar";
                pathToAvatars = Constants.PathsToFiles.pathToAvatars;
                break;
            case "marketolog":
                userCode =2;
                strForMD5 = compRep.findById(marketRep.findByAccountID(accRep.findBylogin(login).getId()).getCompanyID()).getTitle()+"-avatar";
                pathToAvatars = Constants.PathsToFiles.pathToCompaniesPhotos;
                break;
        }
        if (!file.isEmpty()) {
           try{
               //определяем структуру хранения
               name = ImageHandler.getMD5(strForMD5);
               String externalFolder = name.substring(0,2);
               String internalFolder = name.substring(2,4);
               name =  name.substring(4)+".jpg";
               pathToDB = "/"+externalFolder+"/"+internalFolder+"/"+name;
               //Проверка на наличие папок, и создание, если требуется
               File theDir = new File(pathToAvatars+externalFolder);
               if (!theDir.exists()) {
                       theDir.mkdir();

               }
               theDir = new File(pathToAvatars+externalFolder+"\\"+internalFolder);
               if (!theDir.exists()) {
                   theDir.mkdir();
                }
                //Запись файла
                byte[] bytes = file.getBytes();
                pathname =pathToAvatars+externalFolder+"\\"+internalFolder+"\\"+name;

                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(pathname)));
                stream.write(bytes);
                stream.close();

               responseCode=1;
            } catch (Exception e) {
               responseCode=-1;
            }

        } else {
            responseCode=0;
        }
        switch (responseCode){
            case 1:
                response = "Загрузка завершена";
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();

                // Запись ссылки на аватар в таблицу аккаунтов
                if (userCode.equals(1))
                accRep.setPathToAvatar(login,pathToDB,date);
                if (userCode.equals(2))
                    compRep.setPathToAvatar(marketRep.findByAccountID(accRep.findBylogin(login).getId()).getCompanyID(),pathToDB,date);
                break;
            case 0:
                response = "Файл пустой";
                break;
            case -1:
                response = "Ошибка загрузки";
                break;
        }
        return responseCode;
    }

    @RequestMapping(value="/downloadavatar", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody  byte[] downloadAvatar(@RequestParam(value ="login",required = true,defaultValue = "") String login) {
        String path = accRep.findBylogin(login).getPathToAvatar();
        File file = new File(path);

        InputStream in = null;
        try {
            in = FileUtils.openInputStream(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value="/avatar/getpath", method=RequestMethod.GET)
    @ResponseBody
    public Account getAvatarPath(@RequestParam(value ="login",required = true,defaultValue = "") String login) {
        return accRep.findBylogin(login);
        }

    @RequestMapping(value="/avatar/getpathfriendwithract", method=RequestMethod.GET)
    @ResponseBody
    public List<Account> getAvatarPathFriendsWithRAct(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                                      @RequestParam(value ="actionID",required = true,defaultValue = "") Long actionID) {
        return services.getAvatarPathFriendsWithRAct(login,actionID);
    }

    @RequestMapping(value="/avatar/getpathfriendwithractincompany", method=RequestMethod.GET)
    @ResponseBody
    public List<Account> getAvatarPathFriendsWithActInCompany(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                                      @RequestParam(value ="companyname",required = true,defaultValue = "") String companyName) {
        return services.getAvatarPathFriendsWithRActInCompany(login,companyName);
    }

    @RequestMapping(value="/avatar/getpathfriendhelped", method=RequestMethod.GET)
    @ResponseBody
    public List<Account> getAvatarPathFriendsHelped(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                                    @RequestParam(value ="actionID",required = true,defaultValue = "") Long actionID) {
        return services.getAvatarPathFriendsHelped(login,actionID);
    }

    @RequestMapping(value="/avatar/getpathcommonfriend", method=RequestMethod.GET)
    @ResponseBody
    public List<Account> getAvatarPathCommonFriends(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                                    @RequestParam(value ="friendLogin",required = true,defaultValue = "") String friendLogin) {
        return services.getAvatarPathCommonFriends(login,friendLogin);
    }


    @RequestMapping(value="/companyavatar/getpath", method=RequestMethod.GET)
    @ResponseBody
    public Company getCompanyAvatarPath(@RequestParam(value ="companyname",required = true,defaultValue = "") String companyName) {
        return compRep.findByTitle(companyName);
    }
}
