package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.entity.ActionAccess;
import com.sarafanka.team.sarafanka.server.entity.Company;
import com.sarafanka.team.sarafanka.server.entity.CustomMap;
import com.sarafanka.team.sarafanka.server.repository.ActionRepository;
import com.sarafanka.team.sarafanka.server.repository.CompaniesRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class CompaniesController {
    @Autowired
    private CompaniesRepository companiesRepository;
    @Autowired
    private ActionRepository actRepo;
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/companies/getall", method = RequestMethod.GET)
    @ResponseBody
    public List<Company> getAllCompanies(){
        return companiesRepository.findAll();
    }

    @RequestMapping(value = "/companies/getcompanybyuserid", method = RequestMethod.GET)
    @ResponseBody
    public List<Company> getCompanyByUserId(@RequestParam(value ="login",required = true,defaultValue = "") String lgn, @RequestParam(value ="ifcomplited",required = true,defaultValue = "") Integer ifComplited)
    {
        return services.getCompanyWithRunningAction(lgn, ifComplited);
    }

    @RequestMapping(value = "/companies/registration", method = RequestMethod.GET)
    @ResponseBody
    public String createNewCompany(@RequestParam(value ="email",required = true,defaultValue = "") String email,
                                          @RequestParam(value ="pass",required = true,defaultValue = "") String pass,
                                          @RequestParam(value ="company",required = true,defaultValue = "") String companyName)
    {
        return services.createNewCompany(email,pass,companyName);
    }

    @RequestMapping(value = "/companies/getcompanywithaction", method = RequestMethod.GET)
    @ResponseBody
    public List<Company> getCompanyWithAction(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID)
    {
        return services.getCompanyWithAction(accountID);
    }

    @RequestMapping(value = "/companies/getcompanybycategory", method = RequestMethod.GET)
    @ResponseBody
    public List<Company> getCompanyByCategory(@RequestParam(value ="category",required = true,defaultValue = "") String category,
                                              @RequestParam(value ="accountid",required = true,defaultValue = "")  Long accountID)
    {
        return services.getCompanyByCategory(category,accountID);
    }

    @RequestMapping(value = "/companies/getmarketologcompany", method = RequestMethod.GET)
    @ResponseBody
    public Company getMarketologCompany(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID)
    {
        return services.getMarketologCompany(accountID);
    }

    @RequestMapping(value = "/companies/getcompanybyid", method = RequestMethod.GET)
    @ResponseBody
    public Company getCompanyById(@RequestParam(value ="id",required = true,defaultValue = "") Long id)
    {
        return companiesRepository.findById(id);
    }

    @RequestMapping(value = "/companies/changecompanyinfobymain", method = RequestMethod.GET)
    @ResponseBody
    public Integer changeCompanyInfoByMain(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                           @RequestParam(value ="newName",required = true,defaultValue = "") String newName,
                                           @RequestParam(value ="newrealtype",required = true,defaultValue = "") String newRealType,
                                           @RequestParam(value ="newtype",required = true,defaultValue = "") String newType,
                                           @RequestParam(value ="newdescription",required = true,defaultValue = "") String newDescription,
                                           @RequestParam(value ="newAdress",required = true,defaultValue = "") String newAdress,
                                           @RequestParam(value ="newphone",required = true,defaultValue = "") String newPhone,
                                           @RequestParam(value ="newsite",required = true,defaultValue = "") String newSite)
    {
        return services.changeCompanyInfoByMain(login,newName,newRealType,newType,newDescription,newAdress,newPhone,newSite);
    }

    @RequestMapping(value = "/companies/changecompanyinfobymain", method = RequestMethod.POST)
    @ResponseBody
    public Integer changeCompanyInfoByMain(@RequestBody Company newBrandInfo)
    {
        return services.changeCompanyInfoByMain(newBrandInfo);
    }

    @RequestMapping(value = "/companies/getcompanybyarr", method = RequestMethod.POST)
    @ResponseBody
    public List<CustomMap> getEstByArr(@RequestBody Long[] actionIDs){
        List<CustomMap>comMap = new ArrayList<>();
        for (Long i: actionIDs) {
            comMap.add(new CustomMap(i,companiesRepository.findById(actRepo.findById(i).getOrganizationID()).getTitle()));
        }

        return comMap;
    }

    @RequestMapping(value = "/companies/getfullcompanybyarr", method = RequestMethod.POST)
    @ResponseBody
    public List<Company> getFullCompByArr(@RequestBody Long[] actionIDs){
        List<Company> comMap = new ArrayList<>();
        for (Long i: actionIDs) {
            comMap.add(companiesRepository.findById(actRepo.findById(i).getOrganizationID()));
        }

        return comMap;
    }
}
