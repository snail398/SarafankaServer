package com.sarafanka.team.sarafanka.server.service;

import com.google.zxing.WriterException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sarafanka.team.sarafanka.server.Constants;
import com.sarafanka.team.sarafanka.server.ImageHandler;
import com.sarafanka.team.sarafanka.server.QRGenerator;
import com.sarafanka.team.sarafanka.server.entity.*;
import com.sarafanka.team.sarafanka.server.repository.*;
import com.sarafanka.team.sarafanka.server.socialSenders.SmsSender;
import com.sarafanka.team.sarafanka.server.socialSenders.SocialSender;
import com.sarafanka.team.sarafanka.server.socialSenders.WhatappSender;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiogasolutions.apis.bitly.BitlyApis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Service
public class ServicesImpl implements Services {

    @Autowired
    private RunningActionsRepository repo;
    @Autowired
    private ActionRepository actRepo;
    @Autowired
    private FriendsRepository friendRepo;
    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private BarmenRepository barmenRepo;
    @Autowired
    private InvitesRepositories invRepo;
    @Autowired
    private CompaniesRepository comRepo;
    @Autowired
    private CouponsRepository couponsRepo;
    @Autowired
    private MarketologRepository marketologRepo;
    @Autowired
    private ActionAccessRepository actionAccessRepository;
    @Autowired
    private MarketologWorkingPlaceRepository marketologWorkingPlaceRepository;
    @Autowired
    private BarmenWorkingPlaceRepository barmenWorkingPlaceRepository;
    @Autowired
    private EstablishmentRepository establishmentRepository;
    @Autowired
    private StaffOperationHistoryRepository staffOperationHistoryRepository;
    @Autowired
    private CookieAndSessionRepository cookieAndSessionRepository;

    @Override
    public List<RunningActions> getRunningActionsByAccountID(Long accountID) {
        List<RunningActions> racts = repo.findAllByAccountLoginID(accountID);
        racts.sort(new Comparator<RunningActions>() {
            @Override
            public int compare(RunningActions o1, RunningActions o2) {
                return o2.getRactStatDate().compareTo(o1.getRactStatDate());
            }
        });
        return racts;
    }

    @Override
    public Integer addBarmenOperation(Long staffID, String type, Long clientID, Long actionID) {
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        StaffOperationHistory newOperation = new StaffOperationHistory();
        newOperation.setStaffAccountID(staffID);
        newOperation.setActionID(actionID);
        newOperation.setClientAccountID(clientID);
        newOperation.setOperationType(type);
        newOperation.setOperationDate(date);
        staffOperationHistoryRepository.saveAndFlush(newOperation);
        return 1;
    }

    @Override
    public List<StaffOperationHistoryForApp> getBarmenOperations(Long staffID) {
        List<StaffOperationHistory> history = staffOperationHistoryRepository.findByStaffAccountID(staffID);
        List<StaffOperationHistoryForApp> historyForApp = new ArrayList<>();
        history.sort(new Comparator<StaffOperationHistory>() {
            @Override
            public int compare(StaffOperationHistory o1, StaffOperationHistory o2) {
                return o2.getOperationDate().compareTo(o1.getOperationDate());
            }
        });
        for (StaffOperationHistory operation: history) {
            if (operation.getStaffAccountID().equals(staffID)) {
                StaffOperationHistoryForApp operationForApp = new StaffOperationHistoryForApp();
                operationForApp.setStaffAccount(accRepo.findByid(operation.getStaffAccountID()).getLogin());
                if(accRepo.findByid(operation.getClientAccountID())==null) continue;
                String clientName=accRepo.findByid(operation.getClientAccountID()).getLogin();
                if (accRepo.findByid(operation.getClientAccountID()).getLogin().equals("Ваш E-mail"))
                    clientName = accRepo.findByid(operation.getClientAccountID()).getPhoneNumber();
                operationForApp.setClientAccount(clientName);
                operationForApp.setOperationType(operation.getOperationType());
                operationForApp.setOperationDate(operation.getOperationDate());

                if(actRepo.findById(operation.getActionID())==null) continue;
                operationForApp.setAction(actRepo.findById(operation.getActionID()).getReward());
                historyForApp.add(operationForApp);
            }
        }
        return historyForApp;
    }

    @Override
    public Integer registrationMarketolog(Long creatorID, String lgn, String pass, String firstname, String secondname, String estAdress) {

        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim();
        String targetPass = pass.trim();
        Integer responseCode = 0;
        Boolean duplicate =false;

        //Получаем список всех аккаунтов
        List<Account> list = accRepo.findAll();
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
            newAcc.setAccountType("marketolog");
            newAcc.setFirstName(firstname);
            newAcc.setSecondName(secondname);
            newAcc.setCreatingDate(date);
            newAcc.setPathToAvatar("noavatar");
            newAcc.setPhoneNumber("0");
            accRepo.saveAndFlush(newAcc);
            Marketolog marketolog = new Marketolog();
            marketolog.setAccountID(accRepo.findBylogin(targetLogin).getId());
            marketolog.setMain(0);
            marketolog.setCompanyID(marketologRepo.findByAccountID(creatorID).getCompanyID());
            marketologRepo.saveAndFlush(marketolog);
            if (!estAdress.equals("Все заведения")) {
                MarketologWorkingPlace mwp = new MarketologWorkingPlace();
                mwp.setEstablishmentID(establishmentRepository.findByEstName(estAdress).getId());
                mwp.setMarketologID(marketologRepo.findByAccountID(accRepo.findBylogin(targetLogin).getId()).getId());
                marketologWorkingPlaceRepository.saveAndFlush(mwp);
            }
            else {
                for (Establishment est: establishmentRepository.findByCompanyID(marketologRepo.findByAccountID(creatorID).getCompanyID())) {
                    MarketologWorkingPlace mwp = new MarketologWorkingPlace();
                    mwp.setEstablishmentID(est.getId());
                    mwp.setMarketologID(marketologRepo.findByAccountID(accRepo.findBylogin(targetLogin).getId()).getId());
                    marketologWorkingPlaceRepository.saveAndFlush(mwp);
                }
            }

            responseCode =1;
        }
        else{
            responseCode = -1;
        }
        return  responseCode;
    }

    @Override
    public Integer registrationMainMarketolog(String lgn, String pass, String firstname, String secondname, String companyName,String companyCategory, String companyType, String companyDescription, String companyAdress, String companyPhone, String companySite,Establishment newEst) {
        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim();
        String targetPass = pass.trim();
        Integer responseCode = 0;
        Boolean duplicate =false;

        //Получаем список всех аккаунтов
        List<Account> list = accRepo.findAll();
        //Поиск аккаунта в списке (Проверка на дублирование логинов)
        for (Account acc:list) {
            if (acc.getLogin().equals(targetLogin)){
                duplicate = true;
                responseCode=-1;
                break;
            }
        }
        List<Company> list1 = comRepo.findAll();

        for (Company com:list1) {
            if (com.getTitle().equals(companyName)){
                duplicate = true;
                responseCode = -2;
                break;
            }
        }
        //Если нет такого логина, созадаем новый аккаунт
        if(!duplicate){
            Calendar c = Calendar.getInstance();
            Long date = c.getTimeInMillis();
            //Создание аккаунта

            Account newAcc = new Account();
            newAcc.setLogin(targetLogin);
            newAcc.setPassword(targetPass);
            newAcc.setAccountType("marketolog");
            newAcc.setFirstName(firstname);
            newAcc.setSecondName(secondname);
            newAcc.setCreatingDate(date);
            newAcc.setPathToAvatar("noavatar");
            newAcc.setPhoneNumber("0");
            accRepo.saveAndFlush(newAcc);
            //Создание компании
            Company newCompany = new Company();
            newCompany.setTitle(companyName);
            newCompany.setCategory(companyCategory);
            newCompany.setCompanyType(companyType);
            newCompany.setDescription(companyDescription);
            newCompany.setAdress(companyAdress);
            newCompany.setPhone(companyPhone);
            newCompany.setSite(companySite);
            newCompany.setCreatingDate(date);
            comRepo.saveAndFlush(newCompany);
            //Создание маркетолога
            Marketolog marketolog = new Marketolog();
            marketolog.setAccountID(accRepo.findBylogin(targetLogin).getId());
            marketolog.setMain(1);
            marketolog.setCompanyID(comRepo.findByTitle(companyName).getId());
            marketologRepo.saveAndFlush(marketolog);
            establishmentRepository.saveAndFlush(newEst);

            responseCode =1;
        }

        return  responseCode;
    }

    public List<Action> getRunningActionsByLoginAndOrgId(String lgn, Long orgId, Integer ifComplited) {
        List<RunningActions> allRunningActions = repo.findAll();
        List<Long> targetActions = new ArrayList<Long>();
        for (RunningActions ract:allRunningActions) {
            if (accRepo.findOne(ract.getAccountLoginID()).getLogin().equals(lgn) && ract.getComplited().equals(ifComplited)){
                targetActions.add(actRepo.findOne(ract.getActionTitleID()).getId());
            }
        }
        List<Action> allActions= actRepo.findAll();
        List<Action> list = new ArrayList<Action>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Long str:targetActions) {
            for (Action act:allActions) {
                if (act.getId() == str && act.getOrganizationID().equals(orgId)&&date>act.getTimeStart()&&date<act.getTimeEnd()){
                    list.add(act);
                    continue;
                }
            }
        }
        return list;
    }

    @Override
    public Integer deleteFriend(String lgn, String friend) {
        for (Friends friend1: friendRepo.findAll()) {
            if ((friend1.getUser1_id().equals(accRepo.findBylogin(lgn).getId())&&friend1.getUser2_id().equals(accRepo.findBylogin(friend).getId()))||
                    (friend1.getUser2_id().equals(accRepo.findBylogin(lgn).getId())&&friend1.getUser1_id().equals(accRepo.findBylogin(friend).getId())) ){
                friendRepo.delete(friend1);
            }
        }
        return 1;
    }

    public Integer addNewRunningActions(String lgn, Long id) {

        actRepo.increasePeopleUser(id);

        RunningActions runAct = new RunningActions();
        runAct.setAccountLoginID(accRepo.findBylogin(lgn).getId());
        runAct.setActionTitleID(id);
        runAct.setPercentOfComplete(0);
        runAct.setComplited(0);
        repo.saveAndFlush(runAct);
        return 2;
    }

    @Override
    public Integer addNewRunningActions(Account acc, Long id, String messageType,Long staffID) throws IOException, WriterException {

        Boolean duplicateEmail =false;
        Boolean duplicatePhone =false;
        Boolean duplicateRunAct =false;

        Calendar c1 = Calendar.getInstance();
        Long date1 = c1.getTimeInMillis();
        acc.setCreatingDate(date1);
        //форматирование телефона (Удаление дефисов,пробелов,скобок
        String phone = acc.getPhoneNumber();
        acc.setPhoneNumber(phone.replace(" ","").replace("-","").replace("(","").replace(")","").replace("+",""));
        Account responseAccount = new Account();
        responseAccount.setAccountType("none");
        List<Account> list = accRepo.findAll();
        //Поиск аккаунта в списке (Проверка на дублирование логинов)
        for (Account tempAcc:list) {
            if (tempAcc.getLogin().equals(acc.getLogin()) && !tempAcc.getLogin().equals("Ваш E-mail")){
                duplicateEmail = true;
                responseAccount = tempAcc;
                break;
            }
        }
        for (Account tempAcc:list) {
            if (tempAcc.getPhoneNumber().equals(acc.getPhoneNumber())){
                duplicatePhone = true;
                responseAccount = tempAcc;
                break;
            }
        }
        //Если нет такого логина, созадаем новый аккаунт
        if(!duplicateEmail && !duplicatePhone){
            accRepo.saveAndFlush(acc);
            responseAccount = accRepo.findByPhoneNumber(acc.getPhoneNumber());
        }

        actRepo.increasePeopleUser(id);
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        RunningActions runAct = new RunningActions();
        runAct.setAccountLoginID(responseAccount.getId());
        runAct.setActionTitleID(id);
        runAct.setPercentOfComplete(0);
        runAct.setComplited(0);
        runAct.setRactStatDate(date);
        for (RunningActions runningActions:repo.findAll() ) {
            if (runningActions.getActionTitleID().equals(runAct.getActionTitleID()) && runningActions.getAccountLoginID().equals(runAct.getAccountLoginID()) && runningActions.getComplited().equals(0)) {
                duplicateRunAct = true;
                runAct = runningActions;
            }
        }

        if (!duplicateRunAct)
        repo.saveAndFlush(runAct);

        StaffOperationHistory newOperation = new StaffOperationHistory();
        newOperation.setStaffAccountID(staffID);
        newOperation.setActionID(runAct.getActionTitleID());
        newOperation.setClientAccountID(runAct.getAccountLoginID());
        newOperation.setOperationType("Запустил акцию");
        newOperation.setOperationDate(date);
        staffOperationHistoryRepository.saveAndFlush(newOperation);

        BitlyApis bitlyApis = new BitlyApis(Constants.Social.bitlyAccessToken);

        generateQRforRact("sarafunka",runAct.getAccountLoginID(), runAct.getActionTitleID());
        generateSarafunkaforRact("sarafunka",runAct.getAccountLoginID(), runAct.getActionTitleID());

        String textToSend = Constants.URL.HOST +"/getract?ractid="+runAct.getId();
        String shortURL = bitlyApis.shortenEncodedUrl(textToSend);
        SocialSender socialSender = createSocialSender(messageType);
        String result = socialSender.send("Ваш личный кабинет участия в акции: "+shortURL+"\nДля участия ответьте \"ОК\""   ,responseAccount.getPhoneNumber());
        switch ( result){
            case "false":
                return -2;

            case "true":
                return 2;

        }

        return -2;
    }

    @Override
    public String getLink(String userPhone, Long actionID, Long barmenID) throws IOException {
        BitlyApis bitlyApis = new BitlyApis(Constants.Social.bitlyAccessToken);
        RunningActions  runAct = repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accRepo.findByPhoneNumber(userPhone.replace(" ","").replace("-","").replace("(","").replace(")","").replace("+","")).getId(),0);
        String textToSend = Constants.URL.HOST +"/getract?ractid="+runAct.getId();
        return bitlyApis.shortenEncodedUrl(textToSend);
    }

    @Override
    public String getLinkAndBonus(Long userID, Long actionID, Long barmenID) throws IOException {
        BitlyApis bitlyApis = new BitlyApis(Constants.Social.bitlyAccessToken);
        Coupon  newCoupon = new Coupon();
        List<Coupon> list = couponsRepo.findAllByAccountIDAndActionID(userID,actionID);
        newCoupon = list.get(list.size()-1);
        String textToSend = Constants.URL.HOST +"/sarafunkas/"+newCoupon.getPathToSarafunka();
        return accRepo.findByid(userID).getPhoneNumber()+bitlyApis.shortenEncodedUrl(textToSend)+"|"+actRepo.findById(newCoupon.getActionID()).getSupportReward();
    }

    public SocialSender createSocialSender(String messageType) {
        SocialSender socialSender;
        switch (messageType){
            case "sms":
                socialSender = new SmsSender();
                break;
            case "whatsapp":
                socialSender = new WhatappSender();
                break;
                default:
                    socialSender = new SmsSender();
                    break;
        }
        return socialSender;
    }

    //тип сарафанки: sarafunka - для приглашения друзей, coupon - для аффилиата
    public void generateQRforRact(String sarafankaType, Long accountID, Long actionID) throws IOException, WriterException {
        Account currentAccount = accRepo.findByid(accountID);
        Action currentAction =  actRepo.findById(actionID);

        String name = "";
        String pathToQRCodes = "";
        String pathname ="sosiska";
        String pathToDB ="";
        int responseCode;
        String response ="";
        String strForMD5 ="";
        Integer userCode= 0;
        Coupon coupon = new Coupon();
        switch (sarafankaType){
            case "sarafunka":
                strForMD5 = repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getId()+"-qrcode";
                break;
            case "coupon":
                List<Coupon> list = couponsRepo.findAllByAccountIDAndActionID(accountID,actionID);
                coupon = list.get(list.size()-1);
                strForMD5 = coupon.getId()+"-qrcodecoupon";
                break;
        }
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
            theDir.     mkdir();
        }

        pathname =pathToQRCodes+externalFolder+"\\"+internalFolder+"\\"+name;

        QRGenerator qrGenerator = new QRGenerator();
        String strForQR ="";
        switch (sarafankaType){
            case "sarafunka":
                RunningActions ract = repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0);
                strForQR = "/runningactions/changeprogressforsocial?userid=" + currentAccount.getId() + "&actionid=" + currentAction.getId();
                qrGenerator.generateQRCodeImage(strForQR, 540, 540, pathname);
                ract.setPathToQRCode(externalFolder+"\\"+internalFolder+"\\"+name);
                repo.setPathToQR(ract.getId(),externalFolder+"\\"+internalFolder+"\\"+name);
                break;
            case "coupon":
                strForQR = "/coupons/deleteusedcoupon?accountid=" + currentAccount.getId() + "&actionid=" + currentAction.getId();
                    qrGenerator.generateQRCodeImage(strForQR, 540, 540, pathname);
                coupon.setPathToQRCode(externalFolder+"\\"+internalFolder+"\\"+name);
                couponsRepo.setPathToQR(coupon.getId(),externalFolder+"\\"+internalFolder+"\\"+name);
                break;
        }

    }
    public void generateSarafunkaforRact(String sarafankaType, Long accountID, Long actionID) throws IOException, WriterException {
        Account currentAccount = accRepo.findByid(accountID);
        Action currentAction =  actRepo.findById(actionID);
        String name = "";
        String pathToQRCodes = "";
        String pathname ="sosiska";
        String pathToDB ="";
        int responseCode;
        String response ="";
        String strForMD5 ="";
        Integer userCode= 0;

        Coupon coupon = new Coupon();

        switch (sarafankaType){
            case "sarafunka":
                strForMD5 = repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getId()+"-sarafunka";
                break;
            case "coupon":
                List<Coupon> list = couponsRepo.findAllByAccountIDAndActionID(accountID,actionID);
                coupon = list.get(list.size()-1);
                strForMD5 = coupon.getId()+"-coupon";
                break;
        }
        pathToQRCodes = Constants.PathsToFiles.pathToSarafunkas;


        name = ImageHandler.getMD5(strForMD5);
        String externalFolder = name.substring(0,2);
        String internalFolder = name.substring(2,4);
        name =  name.substring(4)+".pdf";
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

        switch (sarafankaType){
            case "sarafunka":
                RunningActions ract = repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0);
                repo.setPathToSarafunka(ract.getId(),externalFolder+"\\"+internalFolder+"\\"+name);
                ract.setPathToSarafunkaForFriend(externalFolder+"\\"+internalFolder+"\\"+name);
                break;
            case "coupon":
                couponsRepo.setPathToSarafunka(coupon.getId(),externalFolder+"\\"+internalFolder+"\\"+name);
                coupon.setPathToSarafunka(externalFolder+"\\"+internalFolder+"\\"+name);

                break;
        }

        createPDF(sarafankaType,accountID,actionID,pathname);

    }


    // Первый символ : S- сарафанка для друзей,C - сарафанка для аффилиата за задание
    // Следующие четыре символа - id аккаунта
    // Последние 5 символов - id акции
    String createUniqID(String sarafankaType, Long accountID, Long actionID){
        String userString ="0000";
        String actionString="00000";
        String firstSymbol="";
        userString = userString.substring(0,userString.length()-accountID.toString().length())+accountID.toString();
        actionString = actionString.substring(0,actionString.length()-actionID.toString().length())+actionID.toString();

        switch (sarafankaType){
            case "sarafunka":
                firstSymbol="S";
                break;
            case "coupon":
                firstSymbol ="C";
                break;
        }
        return firstSymbol+userString+actionString;
    }
    public void createPDF(String sarafankaType, Long accountID, Long actionID,String path){

        Action action = actRepo.findById(actionID);
        Company company = comRepo.findById(action.getOrganizationID());
        String price="";
        String title ="";
        String advice ="";
        String companyName = "";
        String companyAddress = "";
        String compamyPhone = "";
        String companySite = "";

        switch (sarafankaType){
            case "sarafunka":
                price = action.getSupportReward();
                title = "Сарафанка от друга";
                advice ="Друг рекомендует";
                break;
            case "coupon":
                price = action.getReward();
                title = "Финальная сарафанка";
                advice ="Бонус можно получить в";
                break;
        }
        if (actionAccessRepository.findActionAccessByActionID(actionID)!=null){
            ActionAccess aa = actionAccessRepository.findActionAccessByActionID(actionID);
            Establishment est  = establishmentRepository.findByid(aa.getEstablishmentID());
            companyName = est.getEstName();
            companyAddress = est.getFactAdress();
            compamyPhone = est.getEstPhone();
            companySite = "www."+est.getEstSite();
        }
        else {
            companyName = company.getTitle();
           // companyAddress = company.getAdress();
           // compamyPhone = company.getPhone();
            companySite = "www." + company.getSite();
        }
        try {
            // Create output PDF
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            // Load existing PDF
            PdfReader reader = new PdfReader(new FileInputStream(Constants.PathsToFiles.pathToSarafunkaTemplate));
            final Random random = new Random();
            PdfImportedPage page = writer.getImportedPage(reader, random.nextInt(4) + 1);

            // Copy first page of existing PDF into output PDF
            document.setPageSize(reader.getPageSize(1));
            Integer sarWidth = (int) reader.getPageSize(1).getWidth();
            Integer sarHeight = (int) reader.getPageSize(1).getHeight();
            document.newPage();
            cb.addTemplate(page, 0, 0);

            //Создание надписей на шаблоне
            BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\Fonts\\ClearSans-Regular.ttf","ISO-8859-5", true);
            cb.setFontAndSize(bf, 100);
            cb.setColorFill(BaseColor.WHITE );

            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, new Phrase(price.toUpperCase(),new Font(bf,30)), 540, 1600, 0);


            Phrase titlePhrase = new Phrase(title,new Font(bf,19.9f));
            Phrase advicePhrase = new Phrase(advice,new Font(bf,13.4f));
            Phrase companyNamePhrase = new Phrase("\""+companyName+"\"",new Font(bf,20));
            Phrase companyAddressPhrase = new Phrase(companyAddress,new Font(bf,13.4f));
            Phrase uniqPhrase = new Phrase(createUniqID(sarafankaType,accountID,actionID),new Font(bf,15));


            Anchor compamyPhonePhrase = new Anchor(
                    compamyPhone,new Font(bf,13.4f));
            compamyPhonePhrase.setReference(
                    "tel: "+compamyPhone);
            Chunk anchorChunk = new Chunk(companySite,new Font(bf,13.4f*1.3f));
            anchorChunk.setUnderline(BaseColor.WHITE,0.2f,0,-2,0,0);
            Anchor anchor = new Anchor(anchorChunk);
            anchor.setReference(
                    companySite);

            Chunk textChunk = new Chunk("Проверить подлинность и срок действия сарафанки:",new Font(bf,10*1.3f));
            Chunk siteChunk = new Chunk("www.sarafun.info",new Font(bf,10*1.3f));
            textChunk.setUnderline(BaseColor.WHITE,0.2f,0,-2,0,0);
            siteChunk.setUnderline(BaseColor.WHITE,0.2f,0,-2,0,0);
            Anchor textAnchor = new Anchor(textChunk);
            Anchor siteAnchor = new Anchor(siteChunk);
            textAnchor.setReference("http://sarafun.info:4200/ractstatus?ractid="+repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getId());
            siteAnchor.setReference("http://sarafun.info:4200/ractstatus?ractid="+repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getId());
            Phrase conditionPhrase = new Phrase("*"+action.getCondition(),new Font(bf,10*1.3f));

            ColumnText.showTextAligned(cb,
                    Element.ALIGN_LEFT, titlePhrase, sarWidth*0.32f, sarHeight*0.907f, 0);

            String[] arr = new String[2];
            Integer rowCol = 0;
            Integer space = 0;
            Float startY=0f;
            if(price.trim().indexOf(" ")!=-1){
                rowCol=2;
                StringBuffer buffer = new StringBuffer(price.substring(0,price.length()/2));
                buffer.reverse();
                Integer startSpace = buffer.indexOf(" ");
                Integer endSpace = price.indexOf(" ",price.length()/2)-price.length()/2;
                if (endSpace<startSpace){
                    space =price.length()/2+endSpace ;
                }
                else{
                    space = price.length()/2-startSpace ;
                }
                if (endSpace.equals(startSpace)){
                    space =price.length()/2-endSpace ;
                }
                arr[0]="На "+price.substring(0,space);
                arr[1]=price.substring(space)+"!*";
                startY=sarHeight*0.78f;
            }
            else{
                rowCol=1;
                arr[0]="На "+price+"!*";
                startY=sarHeight*0.78f-sarHeight*0.055f/2;
            }

            for (int i = 0; i < rowCol; i++) {
                Phrase phrase = new Phrase(arr[i],new Font(bf,25));
                ColumnText.showTextAligned(cb,
                        Element.ALIGN_CENTER, phrase, sarWidth/2, startY-i*sarHeight*0.055f, 0);
            }


            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, uniqPhrase, sarWidth/2, sarHeight*0.257f, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, advicePhrase, sarWidth/2, sarHeight*0.206f+15, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, companyNamePhrase, sarWidth/2, sarHeight*0.161f+15, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, companyAddressPhrase, sarWidth/2, sarHeight*0.132f+15, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, compamyPhonePhrase, sarWidth/2, sarHeight*0.102f+15, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, anchor, sarWidth/2, sarHeight*0.077f+15, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, conditionPhrase, sarWidth/2, sarHeight*0.069f, 0);


            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, textAnchor, sarWidth/2, sarHeight*0.0494f, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, siteAnchor, sarWidth/2, sarHeight*0.0285f, 0);

            String qrURL="";
            String pathToSarafanka ="";
            String fuckingPath=Constants.URL.HOST+"/qrcodes/";
            switch (sarafankaType){
                case "sarafunka":
                    qrURL =fuckingPath+repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getPathToQRCode();
                    pathToSarafanka = repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getPathToSarafunkaForFriend();
                    break;
                case "coupon":
                    Coupon coupon = new Coupon();
                    List<Coupon> list = couponsRepo.findAllByAccountIDAndActionID(accountID,actionID);
                    coupon = list.get(list.size()-1);
                    qrURL =fuckingPath+coupon.getPathToQRCode();
                    pathToSarafanka = coupon.getPathToSarafunka();
                    break;
            }

            Image qr = Image.getInstance(qrURL);
            Float newHeight = 165f;
            Float newWidth = newHeight;
            qr.scaleAbsolute(newWidth,newHeight);
            qr.setAbsolutePosition((sarWidth-newWidth)/2, sarHeight*0.2936f);

            cb.addImage(qr);

            document.close();
            createPDFPNG(pathToSarafanka);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException  e) {
            e.printStackTrace();
        }

        /*
        Action action = actRepo.findById(actionID);
        Company company = comRepo.findById(action.getOrganizationID());
        String price="";
        switch (sarafankaType){
            case "sarafunka":
                price = action.getSupportReward();
                break;
            case "coupon":
                price = action.getReward();
                break;
        }
        String companyName = company.getTitle();
        String companyAddress = company.getAdress();
        String compamyPhone = company.getPhone();
        String companySite = "www."+company.getSite();

        try {

            // Create output PDF
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            // Load existing PDF
            PdfReader reader = new PdfReader(new FileInputStream(Constants.PathsToFiles.pathToSarafunkaTemplate));
            PdfImportedPage page = writer.getImportedPage(reader, 1);

            // Copy first page of existing PDF into output PDF
            document.setPageSize(reader.getPageSize(1));
            document.newPage();
            cb.addTemplate(page, 0, 0);

            //Создание надписей на шаблоне
            BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\Fonts\\Cocon_Regular.otf","ISO-8859-5", true);
            cb.setFontAndSize(bf, 100);
            cb.setColorFill(BaseColor.WHITE );

            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, new Phrase(price.toUpperCase(),new Font(bf,100)), 540, 1600, 0);
            Phrase phrase1 = new Phrase("Предъяви в",new Font(bf,60));
            Phrase phrase2 = new Phrase(companyName,new Font(bf,60));
            Phrase phrase3 = new Phrase(companyAddress,new Font(bf,50));
            Phrase phrase4 = new Phrase(compamyPhone,new Font(bf,50));
            Phrase phrase5 = new Phrase(createUniqID(sarafankaType,accountID,actionID),new Font(bf,45));
            Anchor anchor = new Anchor(
                    companySite,new Font(bf,40));
            anchor.setReference(
                    companySite);

            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, phrase4, 540, 210, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, anchor, 540, 280, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, phrase3, 540, 350, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, phrase2, 540, 420, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, phrase1, 540, 500, 0);
            ColumnText.showTextAligned(cb,
                    Element.ALIGN_CENTER, phrase5, 540, 650, 0);

            // Добавление QR-кода

            String qrURL="";
            String pathToSarafanka ="";
            String fuckingPath=Constants.URL.HOST+"/qrcodes/";
            switch (sarafankaType){
                case "sarafunka":
                    qrURL =fuckingPath+repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getPathToQRCode();
                    pathToSarafanka = repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getPathToSarafunkaForFriend();
                    break;
                case "coupon":
                    qrURL =fuckingPath+couponsRepo.findByAccountIDAndActionID(accountID,actionID).getPathToQRCode();
                    pathToSarafanka = couponsRepo.findByAccountIDAndActionID(accountID,actionID).getPathToSarafunka();

                    break;
            }
            Image qr = Image.getInstance(qrURL);
            qr.setAbsolutePosition(reader.getPageSize(1).getWidth()/2-270, 700);

            cb.addImage(qr);

            document.close();
            createPDFPNG(pathToSarafanka);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException  e) {
            e.printStackTrace();
        }
        */
    }

    public void createPDFPNG(String pathToSarafanka){
        String path =Constants.PathsToFiles.pathToSarafunkas+pathToSarafanka;
        try (final PDDocument document = PDDocument.load(new FileInputStream(path))){
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {

                String pathToSarPNG =pathToSarafanka.substring(0,pathToSarafanka.indexOf("."))+".png";
                String externalFolder = pathToSarPNG.substring(0,2);
                String internalFolder = pathToSarPNG.substring(3,5);
                //Проверка на наличие папок, и создание, если требуется
                File theDir = new File( Constants.PathsToFiles.pathToSarafunkas+"jpg\\"+externalFolder);
                if (!theDir.exists()) {
                    theDir.mkdir();

                }
                theDir = new File( Constants.PathsToFiles.pathToSarafunkas+"jpg\\"+externalFolder+"\\"+internalFolder);
                if (!theDir.exists()) {
                    theDir.mkdir();
                }

                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 100, ImageType.RGB);
                String fileName = Constants.PathsToFiles.pathToSarafunkas+"jpg\\" + pathToSarPNG;
                ImageIOUtil.writeImage(bim, fileName, 100);
            }
            document.close();
        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }
    public List<Account> getFriendsForInvite(String lgn, Long actionID) {
        List<Friends> allFriends = friendRepo.findAll();
        List<Invite> invites = invRepo.findAll();
        List<Account> list = new ArrayList<Account>();
        Long tempId = accRepo.findBylogin(lgn).getId();


        for (Friends fr:allFriends) {
            if (fr.getStatus().equals(1)) {
                if (fr.getUser1_id().equals(tempId)) {
                   // if (invites.size()== 0) {
                //        list.add(accRepo.findOne(fr.getUser2_id()));
                //    }
                    Boolean hasInvite = true;
                    for (Invite invite:invites) {
                        if ((invite.getAction_id()).equals(actionID)&&
                                (invite.getInit_user_id()).equals(tempId)&&
                                (invite.getTarget_user_id()).equals(fr.getUser2_id()
                                )){
                            hasInvite = false;
                        }
                    }
                    if (hasInvite){
                        list.add(accRepo.findOne(fr.getUser2_id()));
                    }
                }
                if (fr.getUser2_id().equals(tempId)) {
                  //  if (invites.size() == 0){
                  //      list.add(accRepo.findOne(fr.getUser1_id()));
                  //  }
                    Boolean hasInvite = true;
                    for (Invite invite:invites) {
                        if ((invite.getAction_id()).equals(actionID)&&
                                (invite.getInit_user_id()).equals(tempId)&&
                                (invite.getTarget_user_id()).equals(fr.getUser1_id())){
                            hasInvite = false;
                        }

                    }
                    if (hasInvite){
                        list.add(accRepo.findOne(fr.getUser1_id()));
                    }
                }
            }
        }
        return list;
    }

    public List<Account> getFriends(String lgn) {
        List<Friends> allFriends = friendRepo.findAll();
        List<Invite> invites = invRepo.findAll();
        List<Account> list = new ArrayList<Account>();
        Long tempId = accRepo.findBylogin(lgn).getId();


        for (Friends fr:allFriends) {
            if (fr.getStatus().equals(1)) {
                if (fr.getUser1_id().equals(tempId)) {
                    list.add(accRepo.findOne(fr.getUser2_id()));
                                   }
                if (fr.getUser2_id().equals(tempId)) {
                            list.add(accRepo.findOne(fr.getUser1_id()));
                }
            }
        }
        return list;
    }
    public List<Account> getRequestFriends(String lgn) {
        List<Friends> allFriends = friendRepo.findAll();
        List<Account> allAccounts = accRepo.findAll();
        List<Account> list = new ArrayList<Account>();
        Long tempId = Long.valueOf(0);
        for (Account acc:allAccounts) {
            if (acc.getLogin().equals(lgn)) {
                tempId=acc.getId();
                break;
            }
        }

        for (Friends fr:allFriends) {
            if (fr.getStatus().equals(0) && fr.getUser2_id().equals(tempId)) {
                list.add(accRepo.findOne(fr.getUser1_id()));
            }
        }
        return list;
    }

    @Override
    public Integer createNewFriendRequest(String initLgn, String targetLgn) {
        Long initAccountID= Long.valueOf(0);
        Long targetAccountID= Long.valueOf(0);
        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(initLgn)){
                initAccountID = acc.getId();

            }
            if (acc.getLogin().equals(targetLgn)){
                targetAccountID = acc.getId();

            }
        }

        Friends newFriendRequest = new Friends();
        newFriendRequest.setUser1_id(initAccountID);
        newFriendRequest.setUser2_id(targetAccountID);
        newFriendRequest.setStatus(0);
        friendRepo.saveAndFlush(newFriendRequest);
        return 1;
    }

    @Override
    public Integer acceptFriendRequest(String accepterLgn, String inviterLgn) {
        Long accepterAccountID= Long.valueOf(0);
        Long inviterAccountID= Long.valueOf(0);
        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(accepterLgn)){
                accepterAccountID = acc.getId();
                continue;
            }
            if (acc.getLogin().equals(inviterLgn)){
                inviterAccountID = acc.getId();
                continue;
            }
        }
       friendRepo.acceptFriendRequest(inviterAccountID,accepterAccountID,Integer.valueOf(1));
        return 1;
    }

    //Получить список людей, приглащающий человека с targetID
    public List<Account> getUsersByInviteTargetID(String targetLogin) {
        Long targetID = accRepo.findBylogin(targetLogin).getId();
        List<Invite> inviteList = invRepo.findAll();
        List<Account> accList = accRepo.findAll();
        List<Account> result = new ArrayList<Account>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();


        for (Invite inv:inviteList) {
            if (inv.getTarget_user_id().equals(targetID) && inv.getResponseCode()==0) {
                Long initUserId = inv.getInit_user_id();
                Action action = actRepo.findById(inv.getAction_id());
                if (date > action.getTimeStart() && date < action.getTimeEnd()){
                    for (Account acc : accList) {
                        Boolean flag = false;
                        if (initUserId.equals(acc.getId())) {
                            for (Account acc1 : result) {
                                if (acc1.getLogin().equals(acc.getLogin())) {
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                result.add(acc);
                                break;
                            }
                        }
                    }
            }
            }
        }
        return result;
    }

    //Получить список акций в которые InitID пригласил человека с targetID
    public List<Action> getInvitedAction(String initLogin, String targetLogin) {
        Long initID = Long.valueOf(0);
        Long targetID = Long.valueOf(0);
        List<Action> result = new ArrayList<Action>();

        List<Invite> inviteList = invRepo.findAll();
        List<Action> actList = actRepo.findAll();

        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(initLogin)){
                initID = acc.getId();
                break;
            }
        }

        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(targetLogin)){
                targetID = acc.getId();
                break;
            }
        }

        for (Invite inv: inviteList) {
            if(inv.getTarget_user_id().equals(targetID)&&inv.getInit_user_id().equals(initID) && inv.getResponseCode()==0) {
                for (Action act: actList) {
                    if(inv.getAction_id().equals(act.getId())){
                        result.add(act);
                        break;
                    }
                }
            }

        }
        return result;
    }

    public List<Action> getFriendsRunningActions(String lgn) {
        List<Action> targetActions = new ArrayList<Action>();
      //  for (Account acc:getFriends(lgn)) {
          //  List<Action> tempAction = getRunningActionsByLoginAndOrgId(acc.getLogin());
         //   for (Action action:tempAction) {
          //      targetActions.add(action);
          //  }
        //}
        return targetActions;
    }

    public List<Company> getCompanyWithRunningAction(String lgn, Integer ifComplited){

        List<Company> result = new ArrayList<Company>();
        List<RunningActions> runActs = repo.findAll();
        List<Action> actions = actRepo.findAll();
        List<Company> companies = comRepo.findAll();
        List<Account> accs = accRepo.findAll();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        //Получение id пользователя по логину
        Long id = Long.valueOf(0);
        for (Account acc: accs) {
            if (acc.getLogin().equals(lgn)){
                id = acc.getId();
                break;
            }
        }

        for (RunningActions runAct: runActs) {
            if (runAct.getAccountLoginID().equals(id) && runAct.getComplited().equals(ifComplited)){
                for (Action act:actions) {
                    if (runAct.getActionTitleID().equals(act.getId())){
                        for (Company company:companies) {
                            if (act.getOrganizationID().equals(company.getId()) &&date > act.getTimeStart() && date < act.getTimeEnd()){
                                Boolean flag = true;
                                for (Company cmp:result ) {
                                    if (cmp.equals(company)) flag =false;
                                }
                                if (flag) result.add(company);
                                break;
                            }
                        }
                     break;
                    }
                }
            }
        }

        return result;
    }

    public Integer ChangeProgress(String lgn,  Long id, String userLogin, String barmenLogin) {
        Long accountID= Long.valueOf(0), actionID= Long.valueOf(0),userAccountID= Long.valueOf(0);
        Action tempAct = new Action();
        Long barmenID = accRepo.findBylogin(barmenLogin).getId();

        accountID = accRepo.findBylogin(lgn).getId();
        actionID = id;
        tempAct = actRepo.findById(id);
        userAccountID = accRepo.findBylogin(userLogin).getId();

        for (Invite invite:invRepo.findAll()) {
            if (invite.getTarget_user_id().equals(userAccountID) &&invite.getInit_user_id().equals(accountID) && invite.getAction_id().equals(actionID)){
               invRepo.delete(invite.getId());
            }
        }

        for (RunningActions ract:repo.findAll() ) {
            if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(actionID)) {
                repo.changeProgress(ract.getId(), ract.getPercentOfComplete() + 1);
                //Создаем запись в истории операций
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();
                StaffOperationHistory newOperation = new StaffOperationHistory();
                newOperation.setStaffAccountID(barmenID);
                newOperation.setActionID(actionID);
                newOperation.setClientAccountID(accountID);
                newOperation.setOperationType("Выдал награду для реферала");
                newOperation.setOperationDate(date);
                if (ract.getPercentOfComplete() >= tempAct.getTarget()-1) {
                    //Выдать купон
                    Coupon newCoupon = new Coupon();
                    newCoupon.setAccountID(accountID);
                    newCoupon.setActionID(actionID);
                    newCoupon.setCompanyID(tempAct.getOrganizationID());
                    newCoupon.setReward(tempAct.getReward());
                    newOperation.setOperationType("Выдал награду для реферала, аффилиат выполнил задание");
                    couponsRepo.saveAndFlush(newCoupon);
                    repo.changeComplitedStatus(ract.getId());

                }

                staffOperationHistoryRepository.saveAndFlush(newOperation);
                break;
            }
        }
        return 1;
    }

    @Override
    public String ChangeProgressForSocial(Long userID, Long actionID, Long barmenID) throws IOException, WriterException {
        String ractBonus="";
        switch (accRepo.findByid(barmenID).getAccountType()){
            case "barmen":
                if (!barmenRepo.findByAccountID(barmenID).getCompanyID().equals(actRepo.findById(actionID).getOrganizationID()))
                    return "noexist";
                break;
            case "marketolog":
                if (!marketologRepo.findByAccountID(barmenID).getCompanyID().equals(actRepo.findById(actionID).getOrganizationID()))
                    return "noexist";
                break;
        }
        Boolean exist = false;
        for (RunningActions ract:repo.findAllByComplited(0) ) {
            if (ract.getAccountLoginID().equals(userID) && ract.getActionTitleID().equals(actionID)) {
                exist = true;
                ractBonus = actRepo.findById(ract.getActionTitleID()).getSupportReward();
                Integer tempPercentOfComplite = ract.getPercentOfComplete() + 1;
                repo.changeProgress(ract.getId(), ract.getPercentOfComplete() + 1);
                //Создаем запись в истории операций
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();

                StaffOperationHistory newOperation = new StaffOperationHistory();
                newOperation.setStaffAccountID(barmenID);
                newOperation.setActionID(actionID);
                newOperation.setClientAccountID(userID);
                newOperation.setOperationType("Выдал награду для реферала");
                newOperation.setOperationDate(date);
                if (tempPercentOfComplite >= actRepo.findById(actionID).getTarget()) {
                    //Выдать купон
                    Coupon newCoupon = new Coupon();
                    newCoupon.setAccountID(userID);
                    newCoupon.setActionID(actionID);
                    newCoupon.setCompanyID(actRepo.findById(actionID).getOrganizationID());
                    newCoupon.setReward(actRepo.findById(actionID).getReward());
                    newOperation.setOperationType("Выдал награду для реферала, аффилиат выполнил задание");
                    couponsRepo.saveAndFlush(newCoupon);
                    //Создать QR-код
                    generateQRforRact("coupon",userID, actionID);
                    //Создать сарафанку
                    generateSarafunkaforRact("coupon",userID, actionID);
                    //Отправить смс со ссылкой на скачивание сарафанки
                    BitlyApis bitlyApis = new BitlyApis(Constants.Social.bitlyAccessToken);

                    String textToSend = Constants.URL.HOST +"/sarafunkas/"+newCoupon.getPathToSarafunka();
                    String shortURL = bitlyApis.shortenEncodedUrl(textToSend);
                    SocialSender socialSender = createSocialSender("whatsapp");
                    repo.changeComplitedStatus(ract.getId());
                    staffOperationHistoryRepository.saveAndFlush(newOperation);
                    String result = socialSender.send("Ссылка на скачивание вашей финальной сарафанки "+shortURL,accRepo.findByid(ract.getAccountLoginID()).getPhoneNumber());
                    switch ( result){
                        case "false":
                            return "error";

                        case "true":
                            return "finish";

                    }

                }

                staffOperationHistoryRepository.saveAndFlush(newOperation);
                break;
            }
        }
        if (exist)
        return ractBonus;
        else{
            /*
            Блок Unlimited Referals
            Удалить в случае отказа от этой системы
            */
            for (RunningActions ract:repo.findAllByComplited(1) ) {
                if (ract.getAccountLoginID().equals(userID) && ract.getActionTitleID().equals(actionID)) {
                    ractBonus = actRepo.findById(ract.getActionTitleID()).getSupportReward();
                    repo.changeProgress(ract.getId(), ract.getPercentOfComplete() + 1);
                    //Создаем запись в истории операций
                    Calendar c = Calendar.getInstance();
                    Long date = c.getTimeInMillis();

                    StaffOperationHistory newOperation = new StaffOperationHistory();
                    newOperation.setStaffAccountID(barmenID);
                    newOperation.setActionID(actionID);
                    newOperation.setClientAccountID(userID);
                    newOperation.setOperationType("Выдал награду для реферала Unlimited Referals");
                    newOperation.setOperationDate(date);
                    staffOperationHistoryRepository.saveAndFlush(newOperation);
                    return ractBonus;
                }
            }
            /*
            Конец блока Unlimited Referals
            */

            return "noexist";
        }
    }

    public Integer addNewInvite(String initLogin, Long actionID, Long[] targetLogins) {
        Long accountID= accRepo.findBylogin(initLogin).getId();

        for (Long i: targetLogins) {
            if (i!=-1) {
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();
                Invite tempInvite = new Invite();
                tempInvite.setInit_user_id(accountID);
                tempInvite.setAction_id(actionID);
                tempInvite.setTarget_user_id(i);
                tempInvite.setInviteDate(date);
                tempInvite.setResponseCode(0);
                invRepo.saveAndFlush(tempInvite);
            }
        }
        return 1;
    }

    public List<Coupon> getCouponsByUserLogin(String lgn) {
        Long accountID= accRepo.findBylogin(lgn).getId();

        List<Coupon> result = new ArrayList<Coupon>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();


        for (Coupon coupon:couponsRepo.findAll()) {
            Action action = actRepo.findById(coupon.getId());
            if (coupon.getAccountID().equals(accountID)&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                result.add(coupon);
            }

        }
        return result;
    }

    public List<Company> getCompanyWithAction(Long accountID) {
        List<Company> result = new ArrayList<Company>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        //Флаг показывает, что пользователь участвует минимум в одной акции компании
        Boolean flag = false;

        for (Company company:comRepo.findAll()) {
            Integer countOfAction = 0;
            Integer countOfRunningAction = 0;
            for (Action action:actRepo.findAll()) {
                if (action.getOrganizationID().equals(company.getId())&&date > action.getTimeStart() && date < action.getTimeEnd() ){
                    countOfAction++;
                    for (RunningActions ract:repo.findAll()) {
                        if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId())&&!ract.getComplited().equals(-1)){
                            countOfRunningAction++;
                            flag = true;
                            break;
                        }
                    }
                }
            }

            if(!countOfRunningAction.equals(countOfAction) && countOfAction>0) {
                result.add(company);
            }
        }

        return result;
    }

    @Override
    public String deleteUsedCoupon(Long accountID, Long actionID, Long staffID) {
        String bonus="";
        boolean exist = false;
              //Удаляем купон
        switch (accRepo.findByid(staffID).getAccountType()){
            case "barmen":
                if (!barmenRepo.findByAccountID(staffID).getCompanyID().equals(actRepo.findById(actionID).getOrganizationID()))
                    return "noexist";
                break;
            case "marketolog":
                if (!marketologRepo.findByAccountID(staffID).getCompanyID().equals(actRepo.findById(actionID).getOrganizationID()))
                    return "noexist";
                break;
        }
        for (Coupon coupon:couponsRepo.findAll()) {
            if (coupon.getAccountID().equals(accountID) &&coupon.getActionID().equals(actionID)){
                couponsRepo.delete(coupon.getId());
                exist = true;
                //Создаем запись в историю операций
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();
                StaffOperationHistory newOperation = new StaffOperationHistory();
                newOperation.setStaffAccountID(staffID);
                newOperation.setActionID(actionID);
                newOperation.setClientAccountID(accountID);
                newOperation.setOperationType("Выдал награду для аффилиата");
                newOperation.setOperationDate(date);
                staffOperationHistoryRepository.saveAndFlush(newOperation);
                bonus = actRepo.findById(coupon.getActionID()).getReward();
                break;
            }
        }
        if (exist)
                return bonus ;
        else return  "noexist";
    }

    @Override
    public List<Company> getCompanyByCategory(String category, Long accountID) {
        List<Company> resultList = new ArrayList<Company>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Company company:comRepo.findAll()) {
            if (company.getCategory().equals(category)){
                Integer countOfAction = 0;
                Integer countOfRunningAction = 0;
                for (Action action:actRepo.findAll()) {
                    if (action.getOrganizationID().equals(company.getId())&&date > action.getTimeStart() && date < action.getTimeEnd() ){
                        countOfAction++;
                        for (RunningActions ract:repo.findAll()) {
                            if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId()) &&!ract.getComplited().equals(-1)){
                                countOfRunningAction++;
                                break;
                            }
                        }
                    }
                }

                if(!countOfRunningAction.equals(countOfAction) && countOfAction>0) {
                    resultList.add(company);
                }
            }
        }

        return resultList;
    }

    @Override
    public Company getMarketologCompany(Long accID) {
        Long companyID = marketologRepo.findByAccountID(accID).getCompanyID();
        return comRepo.findById(companyID);
    }

    @Override
    public Integer getMarketologRole(Long accID) {
        return marketologRepo.findByAccountID(accID).getMain();
    }

    @Override
    public Integer createEstablishment(String login, String adress, String estPhone) {
        Establishment newEst = new Establishment();
        newEst.setFactAdress(adress);
        newEst.setCompanyID(marketologRepo.findByAccountID(accRepo.findBylogin(login).getId()).getCompanyID());
        newEst.setEstPhone(estPhone);
        establishmentRepository.saveAndFlush(newEst);
        return 1;
    }

    @Override
    public Integer changeCompanyInfoByMain(String login, String newName,String newCategory, String newType,String newDescription, String newAdress,String newPhone,String newSite) {
        Long accID = accRepo.findBylogin(login).getId();
        Long companyID = marketologRepo.findByAccountID(accID).getCompanyID();
       // comRepo.changeCompanyInfo(companyID,newName,newCategory,newType,newDescription,newAdress,newPhone,newSite);
        return 1;
    }

    @Override
    public Integer changeCompanyInfoByMain(Company newBrandInfo) {
        return comRepo.changeCompanyInfo(newBrandInfo.getId(),
                                            newBrandInfo.getTitle(),
                                            newBrandInfo.getCategory(),
                                            newBrandInfo.getCompanyType(),
                                            newBrandInfo.getDescription(),
                                            newBrandInfo.getAdress(),
                                            newBrandInfo.getPhone(),
                                            newBrandInfo.getSite(),
                newBrandInfo.getInn(),
                newBrandInfo.getKpp(),
                newBrandInfo.getOgrn());
    }

    @Override
    public Integer addNewActon(String login, String reward, String supportReward, Integer target,String description,Long timeStart,Long timeEnd,String est) {

        Long accID = accRepo.findBylogin(login).getId();

        Action newAction = new Action("Common title",description,marketologRepo.findByAccountID(accID).getCompanyID(),accID,"bringer",reward,supportReward,0,target,timeStart,timeEnd);
        actRepo.saveAndFlush(newAction);
        if (!est.equals("Все заведения")){
            ActionAccess actionAccess = new ActionAccess();
            actionAccess.setActionID(actRepo.findByRewardAndDescription(reward,description).getId());
            actionAccess.setEstablishmentID(establishmentRepository.findByFactAdress(est).getId());
            actionAccessRepository.saveAndFlush(actionAccess);
        }
        return 1;
    }


    @Override
    public Integer addNewActon2(Action action, Long creatorID, String est) {
        Action newAction = new Action("Common title",action.getDescription(),marketologRepo.findByAccountID(creatorID).getCompanyID(),creatorID,"bringer",action.getReward(),action.getSupportReward(),0,action.getTarget(),action.getTimeStart(),action.getTimeEnd());
        actRepo.saveAndFlush(newAction);
        if (!est.equals("Все заведения")){
            ActionAccess actionAccess = new ActionAccess();
            actionAccess.setActionID(newAction.getId());
            actionAccess.setEstablishmentID(establishmentRepository.findByEstName(est).getId());
            actionAccessRepository.saveAndFlush(actionAccess);
        }
        return 1;
    }

    @Override
    public Integer deleteActionByID(Long actionID) {
        actRepo.delete(actionID);
        for (RunningActions ract:repo.findAll()) {
            if (ract.getActionTitleID().equals(actionID)) repo.delete(ract);
        }
        for (Invite invite :invRepo.findAll()) {
            if (invite.getAction_id().equals(actionID)) invRepo.delete(invite);
        }
        return 1;
    }

    @Override
    public Integer getCountOfFriendRequest(String lgn) {
        Integer result = 0;
        Long accountID = accRepo.findBylogin(lgn).getId();
        for (Friends friend:friendRepo.findAll() ) {
            if(friend.getStatus().equals(0) && friend.getUser2_id().equals(accountID)){
                result++;
            }
        }
        return result;
    }

    @Override
    public List<Integer> getCountOfAction(Long accountID) {
       // if (userCookie.equals(cookieAndSessionRepository.findByAccountID(accountID))) {
            List<Company> companies = comRepo.findAll();
            List<Action> actions = actRepo.findAll();
            List<Integer> result = new ArrayList<Integer>();
            Integer number = 0;
            String[] categories = new String[5];
            categories[0] = "smoke";
            categories[1] = "drink";
            categories[2] = "eat";
            categories[3] = "movie";
            categories[4] = "game";

            Integer[] countOfAction = new Integer[categories.length];
            for (int i = 0; i < categories.length; i++) {
                countOfAction[i] = 0;
            }

            List<Long> companiesOfCategory = new ArrayList<Long>();
            Calendar c = Calendar.getInstance();
            for (String category : categories) {
                for (Company company : companies) {
                    if (company.getCategory().equals(category)) {
                        companiesOfCategory.add(company.getId());
                    }
                }
                for (Action action : actions) {
                    for (Long compId : companiesOfCategory) {
                        if (action.getOrganizationID().equals(compId) && action.getTimeStart() < c.getTimeInMillis() && action.getTimeEnd() > c.getTimeInMillis()) {
                            Boolean flag = true;
                            for (RunningActions ract : repo.findAll()) {
                                if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId()) && !ract.getComplited().equals(-1)) {

                                    flag = false;
                                    break;
                                }
                            }
                            if (flag)
                                countOfAction[number]++;
                            break;
                        }
                    }
                }
                number++;
                companiesOfCategory.clear();
            }
            for (int i = 0; i < categories.length; i++) {
                result.add(countOfAction[i]);
            }
/*
        //Получение списка категорий
        categories.add(companies.get(0).getCategory());
        for (Company company:companies) {
            Boolean flag = false;
            for (String category:categories) {
                if (!category.equals(company.getCategory())){
                    flag = true;
                    break;
                }
            }
            if (flag) categories.add(company.getCategory());
        }
*/

            return result;
       // }
     //   else {
            //response.addHeader(HttpStatus.UNAUTHORIZED);
           // return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            //retu
       // }
    }

    @Override
    public Integer getCountPeopleToEnd(String lgn, Long actionID) {

        return (actRepo.findById(actionID).getTarget()-repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accRepo.findBylogin(lgn).getId(),0).getPercentOfComplete());
    }

    @Override
    public Integer deleteAccount(String email) {
        Long accountID = accRepo.findBylogin(email).getId();
        //Удаление аккаунта
        accRepo.delete(accountID);
        //Удаление запущенных им акций
        for (RunningActions ract: repo.findAll()) {
            if (ract.getAccountLoginID().equals(accountID)){
                repo.delete(ract.getId());
            }
        }
        //Удаление дружеских связей
        for (Friends friend: friendRepo.findAll()) {
            if (friend.getUser1_id().equals(accountID) ||friend.getUser2_id().equals(accountID)){
                friendRepo.delete(friend.getId());
            }
        }


        //Удаление неиспользованных купонов
        for (Coupon coupon: couponsRepo.findAll()) {
            if (coupon.getAccountID().equals(accountID)){
                couponsRepo.delete(coupon.getId());
            }
        }

        return 1;
    }

    @Override
    public Integer changeInfo(String oldEmail,String email, String firstname, String secondname) {
        accRepo.changeInfo(oldEmail,email,firstname,secondname);
        return 1;
    }

    @Override
    public Integer getCouponsCount(String lgn) {
        List<Coupon> coupons = couponsRepo.findAll();
        Long accountID = accRepo.findBylogin(lgn).getId();
        Integer result =0;
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        for (Coupon coupon: coupons) {
            Action action = actRepo.findById(coupon.getActionID());
            if (coupon.getAccountID().equals(accountID)&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                result++;
            }
        }
        return result;
    }

    @Override
    public Integer deleteRAact(String lgn, Long actionID) {
        Long accountID = accRepo.findBylogin(lgn).getId();
        for (RunningActions ract:repo.findAll()) {
            if (ract.getActionTitleID().equals(actionID) &&ract.getAccountLoginID().equals(accRepo.findBylogin(lgn))) repo.delete(ract);
        }
        for (Invite invite :invRepo.findAll()) {
            if (invite.getAction_id().equals(actionID) && invite.getInit_user_id().equals(accRepo.findBylogin(lgn).getId())) invRepo.delete(invite);
        }
        return 1;
    }

    @Override
    public List<Account> getAvatarPathFriendsWithRAct(String login, Long actionID) {
        List<Account> friendList = getFriends(login);
        List<Account> friendWithRAct = new ArrayList<>();
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account friend:friendList ) {
            for (RunningActions ract:repo.findAll() ) {
                if (ract.getAccountLoginID().equals(friend.getId()) && ract.getActionTitleID().equals(actionID) &&ract.getComplited().equals(0)){
                    friendWithRAct.add(friend);
                    counter++;
                    break;
                }
                if (counter.equals(friendCount)) break;
            }
        }
        return friendWithRAct;
    }

    @Override
    public List<Account> getAvatarPathFriendsHelped(String login, Long actionID) {
        Long accountID = accRepo.findBylogin(login).getId();
        List<Account> friendList = getFriends(login);
        List<Account> friendHelped = new ArrayList<>();
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account friend:friendList ) {
            for (Invite invite:invRepo.findAll() ) {
                if (invite.getResponseCode() == 1 && invite.getInit_user_id().equals(accountID) && invite.getTarget_user_id().equals(friend.getId()) && invite.getAction_id().equals(actionID)){
                    friendHelped.add(friend);
                    counter++;
                    break;
                }
                if (counter.equals(friendCount)) break;
            }
        }
        return friendHelped;
    }

    @Override
    public List<Account> getAvatarPathFriendsWithRActInCompany(String login, String companyName) {
        List<Account> friendList = getFriends(login);
        List<Account> friendWithRActInCompany = new ArrayList<>();
        List<Action> companyActionList = actRepo.findActionsByOrganizationID(comRepo.findByTitle(companyName).getId());
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account friend:friendList ) {
            for (RunningActions ract:repo.findAll() ) {
                for (Action action: companyActionList) {
                    if (ract.getAccountLoginID().equals(friend.getId()) && ract.getActionTitleID().equals(action.getId()) &&ract.getComplited().equals(0)){
                        friendWithRActInCompany.add(friend);
                        counter++;
                        break;
                    }
                    if (counter.equals(friendCount)) break;
                }
                if (counter.equals(friendCount)) break;
            }
        }
        return friendWithRActInCompany;
    }

    @Override
    public List<Account> getAvatarPathCommonFriends(String login, String friendLogin) {
        List<Account> commonFriends = new ArrayList<>();
        List<Account> userFriendList = getFriends(login);
        List<Account> friendFriendList = getFriends(friendLogin);
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account userFriend:userFriendList) {
            for (Account friendFriend:friendFriendList) {
                    if (userFriend.getLogin().equals(friendFriend.getLogin()    )){
                        counter++;
                        commonFriends.add(userFriend);
                        friendFriendList.remove(friendFriend);
                        break;
                    }
            }
            if (counter.equals(friendCount)) break;
        }
        return commonFriends;
    }

    @Override
    public List<StaffForApp> getMarketologs(Long mainMarketologAccountID) {
        List<StaffForApp> marketologsList = new ArrayList<>();
        Long companyID = marketologRepo.findByAccountID(mainMarketologAccountID).getCompanyID();
        for (Marketolog marketolog: marketologRepo.findAll() ) {
            if (marketolog.getCompanyID().equals(companyID) && marketolog.getMain().equals(0)){
                StaffForApp tempStaff = new StaffForApp();
                tempStaff.setAccountID(accRepo.findByid(marketolog.getAccountID()).getId());
                tempStaff.setStaffName(accRepo.findByid(marketolog.getAccountID()).getFirstName()+" "+accRepo.findByid(marketolog.getAccountID()).getSecondName());
                if (marketologWorkingPlaceRepository.findAllByMarketologID(marketolog.getId()).size() == establishmentRepository.findByCompanyID(marketolog.getCompanyID()).size()){
                    tempStaff.setStaffWorkingPlace("Все заведения");
                }
                else
                if (marketologWorkingPlaceRepository.findAllByMarketologID(marketolog.getId()).size() >1){
                    tempStaff.setStaffWorkingPlace("Несколько заведений");
                }
                else {
                    tempStaff.setStaffWorkingPlace(establishmentRepository.findByid(marketologWorkingPlaceRepository.findByMarketologID(marketolog.getId()).getEstablishmentID()).getFactAdress());                }

                marketologsList.add(tempStaff);
            }
        }
        return marketologsList;
    }

    @Override
    public List<StaffForApp> getBarmens(Long mainMarketologAccountID) {
        List<StaffForApp> marketologsList = new ArrayList<>();
        Long companyID = marketologRepo.findByAccountID(mainMarketologAccountID).getCompanyID();
        for (Barmen barmen: barmenRepo.findAll() ) {
            if (barmen.getCompanyID().equals(companyID)){
                StaffForApp tempStaff = new StaffForApp();
                tempStaff.setAccountID(accRepo.findByid(barmen.getAccountID()).getId());
                tempStaff.setStaffName(accRepo.findByid(barmen.getAccountID()).getFirstName()+" "+accRepo.findByid(barmen.getAccountID()).getSecondName());
                if (barmenWorkingPlaceRepository.findAllByBarmenID(barmen.getId()).size() == establishmentRepository.findByCompanyID(barmen.getCompanyID()).size()){
                    tempStaff.setStaffWorkingPlace("Все заведения");
                }
                else
                if (barmenWorkingPlaceRepository.findAllByBarmenID(barmen.getId()).size() >1){
                    tempStaff.setStaffWorkingPlace("Несколько заведений");
                }
                else {
                    tempStaff.setStaffWorkingPlace(establishmentRepository.findByid(barmenWorkingPlaceRepository.findByBarmenID(barmen.getId()).getEstablishmentID()).getFactAdress());
                }
                marketologsList.add(tempStaff);
            }
        }
        return marketologsList;
    }

    @Override
    public Account getStaff(Long accID) {
        return accRepo.findByid(accID);
    }

    @Override
    public Establishment getAccountsEst(Long accID) {
        Establishment establishment = new Establishment();
        establishment.setEstName("Все заведения");
        switch (accRepo.findByid(accID).getAccountType()){
            case "barmen":
                if (barmenWorkingPlaceRepository.findAllByBarmenID(barmenRepo.findByAccountID(accID).getId()).size()<establishmentRepository.findByCompanyID(barmenRepo.findByAccountID(accID).getCompanyID()).size()){
                    establishment = establishmentRepository.findByid(barmenWorkingPlaceRepository.findByBarmenID(barmenRepo.findByAccountID(accID).getId()).getEstablishmentID());
                }
                break;
            case "marketolog":

                if (marketologWorkingPlaceRepository.findAllByMarketologID(marketologRepo.findByAccountID(accID).getId()).size()<establishmentRepository.findByCompanyID(marketologRepo.findByAccountID(accID).getCompanyID()).size()){
                    establishment = establishmentRepository.findByid(marketologWorkingPlaceRepository.findByMarketologID(marketologRepo.findByAccountID(accID).getId()).getEstablishmentID());
                }
                break;
        }
        return establishment;
    }

    @Override
    public Integer deleteStaff(Long accID) {
        String accType = accRepo.findByid(accID).getAccountType();
        switch (accType){
            case "marketolog":
                for (Marketolog marketolog:marketologRepo.findAll() ) {
                    if (marketolog.getAccountID().equals(accID)){
                        marketologRepo.delete(marketolog);
                        for (MarketologWorkingPlace mwp:marketologWorkingPlaceRepository.findAll()) {
                            if(mwp.getMarketologID().equals(marketolog.getId())){
                                marketologWorkingPlaceRepository.delete(mwp);
                            }
                        }
                        break;
                    }
                }
                break;
            case "barmen":
                for (Barmen barmen:barmenRepo.findAll() ) {
                    if (barmen.getAccountID().equals(accID)){
                        barmenRepo.delete(barmen);
                        for (BarmenWorkingPlace bwp:barmenWorkingPlaceRepository.findAll()) {
                            if(bwp.getbarmenID().equals(barmen.getId())){
                                barmenWorkingPlaceRepository.delete(bwp);
                            }
                        }
                        break;
                    }
                }

                break;
        }
        accRepo.delete(accID);
        return 1;
    }

    @Override
    public List<Action> getActionsForStaff(Account account) {
        List<Action> resultActionsList = new ArrayList<>();
        Long accID = accRepo.findBylogin(account.getLogin()).getId();
        List<Long> actionIDs = new ArrayList<Long>();

        switch (account.getAccountType()){
            case"marketolog":
                Marketolog marketolog = marketologRepo.findByAccountID(accID);
                if(marketolog.getMain().equals(1)){
                    resultActionsList = actRepo.findActionsByOrganizationID(marketolog.getCompanyID());
                }
                else {
                    for (MarketologWorkingPlace mwp:marketologWorkingPlaceRepository.findMarketologWorkingPlacesByMarketologID(marketolog.getId())) {
                        for (ActionAccess aa:actionAccessRepository.findActionAccessesByEstablishmentID(mwp.getEstablishmentID())) {
                            Long id = aa.getActionID();
                            if (id != null) {
                                actionIDs.add(id);
                            }
                        }
                    }
                    for (Long i :actionIDs) {
                        Action act = actRepo.findById(i);
                        if (act != null) {
                            resultActionsList.add(act);
                        }
                    }
                }
                break;
            case "barmen":
                Barmen barmen = barmenRepo.findByAccountID(accID);
                for (BarmenWorkingPlace bwp:barmenWorkingPlaceRepository.findBarmenWorkingPlacesByBarmenID(barmen.getId())) {
                    for (ActionAccess aa:actionAccessRepository.findActionAccessesByEstablishmentID(bwp.getEstablishmentID())) {
                        Long id = aa.getActionID();
                        if (id != null) {
                            actionIDs.add(id);
                        }
                    }
                }
                for (Long i :actionIDs) {
                    Action act = actRepo.findById(i);
                    if (act != null) {
                        resultActionsList.add(act);
                    }
                }
                break;
        }

        resultActionsList.sort(new Comparator<Action>() {
            @Override
            public int compare(Action o1, Action o2) {
                return Long.valueOf(o2.getId()).compareTo(o1.getId());
            }
        });
        return resultActionsList;
    }

    @Override
    public List<ActionStatistic> getStatisticByMarketolog(Long marketologID) {
        List<Action> actions = getActionsForStaff(accRepo.findByid(marketologID));
        List<ActionStatistic> actionStatistics = new ArrayList<>();
        for (Action action:actions) {
            ActionStatistic actionStatistic = new ActionStatistic();
            actionStatistic.setActionID(action.getId());
            actionStatistic.setDescription(action.getDescription());
            actionStatistic.setReward(action.getReward());
            actionStatistic.setSupportReward(action.getSupportReward());
            actionStatistic.setTimeStart(action.getTimeStart());
            actionStatistic.setTimeEnd(action.getTimeEnd());
            actionStatistic.setTarget(action.getTarget());

            actionStatistic.setCountRAct((long) repo.findByActionTitleID(action.getId()).size());
            actionStatistic.setCountComplitedRAct((long) repo.findByActionTitleIDAndComplited(action.getId(), 1).size());
            actionStatistic.setCountMainBonus((long) (repo.findByActionTitleIDAndComplited(action.getId(), 1).size() - couponsRepo.findByActionID(action.getId()).size()));
            Integer countMiniBonus= 0;
            for (RunningActions ract: repo.findByActionTitleID(action.getId())) {
                countMiniBonus+=ract.getPercentOfComplete();
            }
            actionStatistic.setCountMiniBonus(Long.valueOf(countMiniBonus));
            actionStatistics.add(actionStatistic);
        }
        return actionStatistics;
    }

    @Override
    public List<Action> getActionsForMarketolog(String lgn) {
        List<Action> result = new ArrayList<Action>();
        Long accID = accRepo.findBylogin(lgn).getId();
        List<Long> actionIDs = new ArrayList<Long>();

        Marketolog marketolog = marketologRepo.findByAccountID(accID);
        if(marketolog.getMain().equals(1)){
            result = actRepo.findActionsByOrganizationID(marketolog.getCompanyID());
        }
        else {
            for (MarketologWorkingPlace mwp:marketologWorkingPlaceRepository.findMarketologWorkingPlacesByMarketologID(marketolog.getId())) {
                for (ActionAccess aa:actionAccessRepository.findActionAccessesByEstablishmentID(mwp.getEstablishmentID())) {
                    actionIDs.add(aa.getActionID());
                }
            }
            for (Long i :actionIDs) {
                result.add(actRepo.findById(i));
            }
        }
        return result;
    }

    @Override
    public Integer changeStaffInfo(Long accID, String estname, Account account) {
        accRepo.changeStaffInfo(account.getId(),account.getLogin(),account.getFirstName(),account.getSecondName(),account.getPassword());

        switch (accRepo.findByid(account.getId()).getAccountType()){
            case"barmen":
                for (BarmenWorkingPlace bwp: barmenWorkingPlaceRepository.findAllByBarmenID(barmenRepo.findByAccountID(account.getId()).getId())) {
                    barmenWorkingPlaceRepository.delete(bwp);
                }
                if (!estname.equals("Все заведения")){
                    BarmenWorkingPlace bwp = new BarmenWorkingPlace();
                    bwp.setbarmenID(barmenRepo.findByAccountID(account.getId()).getId());
                    bwp.setEstablishmentID(establishmentRepository.findByEstName(estname).getId());
                    barmenWorkingPlaceRepository.saveAndFlush(bwp);
                }
                else {
                    for (Establishment est: establishmentRepository.findByCompanyID(marketologRepo.findByAccountID(accID).getCompanyID())) {
                        BarmenWorkingPlace bwp = new BarmenWorkingPlace();
                        bwp.setbarmenID(barmenRepo.findByAccountID(account.getId()).getId());
                        bwp.setEstablishmentID(est.getId());
                        barmenWorkingPlaceRepository.saveAndFlush(bwp);
                    }
                }
                break;
            case "marketolog":
                for (MarketologWorkingPlace mwp: marketologWorkingPlaceRepository.findAllByMarketologID(marketologRepo.findByAccountID(account.getId()).getId())) {
                    marketologWorkingPlaceRepository.delete(mwp);
                }
                if (!estname.equals("Все заведения")){
                    MarketologWorkingPlace mwp = new MarketologWorkingPlace();
                    mwp.setMarketologID(marketologRepo.findByAccountID(account.getId()).getId());
                    mwp.setEstablishmentID(establishmentRepository.findByEstName(estname).getId());
                    marketologWorkingPlaceRepository.saveAndFlush(mwp);
                }
                else {
                    for (Establishment est: establishmentRepository.findByCompanyID(marketologRepo.findByAccountID(accID).getCompanyID())) {
                        MarketologWorkingPlace mwp = new MarketologWorkingPlace();
                        mwp.setMarketologID(marketologRepo.findByAccountID(account.getId()).getId());
                        mwp.setEstablishmentID(est.getId());
                        marketologWorkingPlaceRepository.saveAndFlush(mwp);
                    }
                }
                break;
        }

        return 1;
    }
}
