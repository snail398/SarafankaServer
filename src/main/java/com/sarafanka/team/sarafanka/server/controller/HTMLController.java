package com.sarafanka.team.sarafanka.server.controller;

import com.google.zxing.WriterException;
import com.sarafanka.team.sarafanka.server.Constants;
import com.sarafanka.team.sarafanka.server.ImageHandler;
import com.sarafanka.team.sarafanka.server.QRGenerator;
import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.entity.RunningActions;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sarafanka.team.sarafanka.server.repository.ActionRepository;
import com.sarafanka.team.sarafanka.server.repository.RunningActionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;


@Controller
public class HTMLController {

    @Autowired
    private RunningActionsRepository repo;
    @Autowired
    private AccountRepository accRep;
    @Autowired
    private ActionRepository actionRepository;

    @RequestMapping("/hello")
    public String welcome(Model model) {
        model.addAttribute("name", "Roma");
        return "hello";
    }

    @RequestMapping("/getract")
    public String getRunningActionPage(@RequestParam(value ="ractid",required = true,defaultValue = "") Long ractID,Model model) throws IOException, WriterException {

        RunningActions currentRunningActions = repo.findByid(ractID);
        Account currentAccount = accRep.findByid(currentRunningActions.getAccountLoginID());
        Action currentAction =  actionRepository.findById(currentRunningActions.getActionTitleID());

        String name = "";
        String pathToQRCodes = "";
        String pathname ="sosiska";
        String pathToDB ="";
        int responseCode;
        String response ="";
        String strForMD5 ="";
        Integer userCode= 0;

        strForMD5 = ractID.toString()+"-qrcode";
        pathToQRCodes = Constants.PathsToFiles.pathToQRCodes;


        name = ImageHandler.getMD5(strForMD5);
        String externalFolder = name.substring(0,2);
        String internalFolder = name.substring(2,4);
        name =  name.substring(4)+".jpg";
        pathToDB = "/"+externalFolder+"/"+internalFolder+"/"+name;
        //Проверка на наличие папок, и создание, если требуется
        File theDir = new File(pathToQRCodes+externalFolder);
        if (!theDir.exists()) {
            theDir.mkdir();

        }
        theDir = new File(pathToQRCodes+externalFolder+"\\"+internalFolder);
        if (!theDir.exists()) {
            theDir.mkdir();
        }

        pathname =pathToQRCodes+externalFolder+"\\"+internalFolder+"\\"+name;

        QRGenerator qrGenerator = new QRGenerator();
        String strForQR = "/runningactions/changeprogressforsocial?userid=" + currentAccount.getId() + "&actionid=" + currentAction.getId();
        String qrPath = qrGenerator.generateQRCodeImage(strForQR, 350, 350, pathname);


        model.addAttribute("phoneNumber", currentAccount.getPhoneNumber());
        model.addAttribute("progress", currentRunningActions.getPercentOfComplete());
        model.addAttribute("target", currentAction.getTarget());
        model.addAttribute("qrpath", "/qrcodes/" +externalFolder+"/"+internalFolder+"/"+name);
        return "RunningActionPage";
    }
}
