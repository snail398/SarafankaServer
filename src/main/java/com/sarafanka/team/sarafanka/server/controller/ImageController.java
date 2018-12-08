package com.sarafanka.team.sarafanka.server.controller;

import com.itextpdf.layout.element.Link;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.sarafanka.team.sarafanka.server.Constants;
import com.sarafanka.team.sarafanka.server.ImageHandler;
import com.sarafanka.team.sarafanka.server.entity.*;
import com.sarafanka.team.sarafanka.server.repository.*;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import com.sarafanka.team.sarafanka.server.socialSenders.SocialSender;
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
import org.tiogasolutions.apis.bitly.BitlyApis;

import javax.annotation.Resource;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@RestController
public class ImageController {

    @Autowired
    private AccountRepository accRep;
    @Autowired
    private ActionRepository actRepo;

    @Autowired
    private CompaniesRepository compRep;
    @Autowired
    private MarketologRepository marketRep;
    @Autowired
    private EstablishmentRepository estRep;
    @Autowired
    private RunningActionsRepository runningActionsRepository;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private Services services = new ServicesImpl();
    @Autowired
    private CouponsRepository couponsRepo;



    @RequestMapping(value="/gen", method=RequestMethod.GET)
    public String createPDF() throws IOException {
        String sarafankaType = "sarafunka";
        Long accountID =Long.valueOf(28);
        Long actionID=Long.valueOf(11);
        String path=Constants.PathsToFiles.pathToSarafunkas+"newSar.pdf";


        Action action = actRepo.findById(actionID);
        Company company = compRep.findById(action.getOrganizationID());
        String price="";
            String title ="";
            String advice ="";
        switch (sarafankaType){
            case "sarafunka":
                price = action.getSupportReward();
                title = "Сарафанка от друга";
                advice ="Друг рекомендует посетить";
                break;
            case "coupon":
                price = action.getReward();
                title = "Финальная сарафанка";
                advice ="Бонус можно получить в";
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
            Phrase advicePhrase = new Phrase(advice,new Font(bf,13.4f*1.3f));
            Phrase companyNamePhrase = new Phrase("\""+companyName+"\"",new Font(bf,20*1.3f));
            Phrase companyAddressPhrase = new Phrase(companyAddress,new Font(bf,13.4f*1.3f));
          //  Phrase phrase5 = new Phrase(createUniqID(sarafankaType,accountID,actionID),new Font(bf,45));
            Phrase uniqPhrase = new Phrase("S000000000",new Font(bf,15));
            Phrase conditionPhrase = new Phrase("*"+action.getCondition(),new Font(bf,10*1.3f));

            Anchor compamyPhonePhrase = new Anchor(
                    compamyPhone,new Font(bf,13.4f*1.3f));
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
            textAnchor.setReference("http://sarafun.info:4200/ractstatus?ractid="+runningActionsRepository.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getId());
            siteAnchor.setReference("http://sarafun.info:4200/ractstatus?ractid="+runningActionsRepository.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getId());



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
                if (endSpace>startSpace){
                    space =price.length()/2+endSpace ;
                }
                else{
                    space =price.length()/2-startSpace ;
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
                    qrURL =fuckingPath+runningActionsRepository.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getPathToQRCode();
                    pathToSarafanka = runningActionsRepository.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getPathToSarafunkaForFriend();
                    break;
                case "coupon":
                    qrURL =fuckingPath+couponsRepo.findByAccountIDAndActionID(accountID,actionID).getPathToQRCode();
                    pathToSarafanka = couponsRepo.findByAccountIDAndActionID(accountID,actionID).getPathToSarafunka();

                    break;
            }

            Image qr = Image.getInstance(Constants.URL.HOST+"/qrcodes/0d/ba/afa501b728ef7525159df32b0b1a.jpg");
            Float newHeight = 165f;
            Float newWidth = newHeight;
            qr.scaleAbsolute(newWidth,newHeight);
            qr.setAbsolutePosition((sarWidth-newWidth)/2, sarHeight*0.2936f);

            cb.addImage(qr);

            document.close();
           // createPDFPNG(pathToSarafanka);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException  e) {
            e.printStackTrace();
        }

        return  "done";

    }

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    @RequestMapping(value="/uploadavatar", method=RequestMethod.POST)
    public @ResponseBody Integer handleFileUpload(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                                  @RequestParam(value ="estname",required = true,defaultValue = "") String estname,
                                                  @RequestBody MultipartFile file){
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
        if (!estname.equals("notest")){
            userCode = 3;
            strForMD5 = estname+"-avatar";
            pathToAvatars = Constants.PathsToFiles.pathToCompaniesPhotos;
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

                // Запись ссылки на аватар в соответствующую таблицу
                if (userCode.equals(1))
                accRep.setPathToAvatar(login,pathToDB,date);
                if (userCode.equals(2))
                    compRep.setPathToAvatar(marketRep.findByAccountID(accRep.findBylogin(login).getId()).getCompanyID(),pathToDB,date);
                if (userCode.equals(3))
                    estRep.setPathToAvatar(estname,pathToDB,date);
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

    @RequestMapping(value="/estavatar/getpath", method=RequestMethod.GET)
    @ResponseBody
    public Establishment getEstAvatarPath(@RequestParam(value ="estname",required = true,defaultValue = "") String estName) {
        return estRep.findByEstName(estName);
    }
}
