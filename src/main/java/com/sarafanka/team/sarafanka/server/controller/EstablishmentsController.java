package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Company;
import com.sarafanka.team.sarafanka.server.entity.Establishment;
import com.sarafanka.team.sarafanka.server.entity.MarketologWorkingPlace;
import com.sarafanka.team.sarafanka.server.repository.AccountRepository;
import com.sarafanka.team.sarafanka.server.repository.CompaniesRepository;
import com.sarafanka.team.sarafanka.server.repository.EstablishmentRepository;
import com.sarafanka.team.sarafanka.server.repository.MarketologRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EstablishmentsController {
    @Autowired
    private EstablishmentRepository estRepository;
    @Autowired
    private AccountRepository accRepository;
    @Autowired
    private MarketologRepository marRepository;
    @Autowired
    private Services services = new ServicesImpl();



    @RequestMapping(value = "/establisments/getestablishmentbycompanyid", method = RequestMethod.GET)
    @ResponseBody
    public List<Establishment> getEstablishmentsByCompanyId(@RequestParam(value ="login",required = true,defaultValue = "") String login)
    {
        return estRepository.findByCompanyID(marRepository.findByAccountID(accRepository.findBylogin(login).getId()).getCompanyID());
    }

    @RequestMapping(value = "/establisments/getestablishmentbycorgid", method = RequestMethod.GET)
    @ResponseBody
    public List<Establishment> getEstablishmentsByCompanyId(@RequestParam(value ="orgid",required = true,defaultValue = "") Long orgID)
    {
        return estRepository.findByCompanyID(orgID);
    }

    @RequestMapping(value = "/establisments/createEstablishment", method = RequestMethod.GET)
    @ResponseBody
    public Integer createEstablishments(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                        @RequestParam(value ="adress",required = true,defaultValue = "") String adress,
                                        @RequestParam(value ="estphone",required = true,defaultValue = "") String estPhone)
    {
        return services.createEstablishment(login,adress,estPhone);
    }

}
