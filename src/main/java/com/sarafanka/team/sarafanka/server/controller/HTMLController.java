package com.sarafanka.team.sarafanka.server.controller;

import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sarafanka.team.sarafanka.server.Constants;
import com.sarafanka.team.sarafanka.server.ImageHandler;
import com.sarafanka.team.sarafanka.server.QRGenerator;
import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.entity.RunningActions;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sarafanka.team.sarafanka.server.repository.ActionRepository;
import com.sarafanka.team.sarafanka.server.repository.RunningActionsRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Calendar;


@Controller
public class HTMLController {

    @Autowired
    private RunningActionsRepository repo;
    @Autowired
    private AccountRepository accRep;
    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private Services services = new ServicesImpl();



    @RequestMapping("/")
    public String choke(Model model) {
        return "choke";
    }

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


        model.addAttribute("phoneNumber", currentAccount.getPhoneNumber());
        model.addAttribute("progress", currentRunningActions.getPercentOfComplete());
        model.addAttribute("target", currentAction.getTarget());
        model.addAttribute("bonus", currentAction.getReward());
        model.addAttribute("qrpath", "/sarafunkas/jpg/" +currentRunningActions.getPathToSarafunkaForFriend().substring(0,currentRunningActions.getPathToSarafunkaForFriend().indexOf("."))+".png");
        model.addAttribute("sarpath", "/sarafunkas/" +currentRunningActions.getPathToSarafunkaForFriend());
        model.addAttribute("vkpath", "https://vk.com/share.php?url=http://sarafun.info:4200/sarafunkas/" +(currentRunningActions.getPathToSarafunkaForFriend()).replace('\\','/'));
        model.addAttribute("wapath", "whatsapp://send?text=http://sarafun.info:4200/sarafunkas/" +(currentRunningActions.getPathToSarafunkaForFriend()).replace('\\','/'));

        return "RunningActionPage";
    }

    @RequestMapping("/ractstatus")
    public String getRunningActionStatus(@RequestParam(value ="ractid",required = true,defaultValue = "") Long ractID,Model model) throws IOException, WriterException {

        RunningActions currentRunningActions = repo.findByid(ractID);
        Account currentAccount = accRep.findByid(currentRunningActions.getAccountLoginID());
        Action currentAction =  actionRepository.findById(currentRunningActions.getActionTitleID());


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentAction.getTimeEnd());
        model.addAttribute("dateend", c.get(Calendar.DATE)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.YEAR));
        model.addAttribute("peopletoend",currentAction.getTarget() - currentRunningActions.getPercentOfComplete());
        model.addAttribute("bonus", currentAction.getSupportReward());
        return "RunningActionStatus";
    }


    @RequestMapping("/getallactionspage")
    public String getAllActionPage(Model model) {
        model.addAttribute("actions", actionRepository.findAll());
        return "staffActionsPage";
    }


    @RequestMapping("/stat")
    public String stat(@RequestParam(value ="ractid",required = true,defaultValue = "") Long ractID,Model model) {
        RunningActions currentRunningActions = repo.findByid(ractID);
        Account currentAccount = accRep.findByid(currentRunningActions.getAccountLoginID());
        Action currentAction =  actionRepository.findById(currentRunningActions.getActionTitleID());


        model.addAttribute("phoneNumber", currentAccount.getPhoneNumber());
        model.addAttribute("progress", currentRunningActions.getPercentOfComplete());
        model.addAttribute("target", currentAction.getTarget());
        model.addAttribute("bonus", currentAction.getReward());
        model.addAttribute("qrpath", "/sarafunkas/jpg/" +currentRunningActions.getPathToSarafunkaForFriend().substring(0,currentRunningActions.getPathToSarafunkaForFriend().indexOf("."))+".png");
        model.addAttribute("sarpath", "/sarafunkas/" +currentRunningActions.getPathToSarafunkaForFriend());
        model.addAttribute("fullsarpath", "http://sarafun.info:4200/sarafunkas/" +currentRunningActions.getPathToSarafunkaForFriend());
        model.addAttribute("vkpath", "https://vk.com/share.php?url=http://sarafun.info:4200/sarafunkas/" +(currentRunningActions.getPathToSarafunkaForFriend()).replace('\\','/'));
        model.addAttribute("wapath", "whatsapp://send?text=http://sarafun.info:4200/sarafunkas/" +(currentRunningActions.getPathToSarafunkaForFriend()).replace('\\','/'));

        return "PdfPage";
    }



}
