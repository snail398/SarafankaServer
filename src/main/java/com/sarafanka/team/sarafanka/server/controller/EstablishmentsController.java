package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.BarmenWorkingPlace;
import com.sarafanka.team.sarafanka.server.entity.Company;
import com.sarafanka.team.sarafanka.server.entity.Establishment;
import com.sarafanka.team.sarafanka.server.entity.MarketologWorkingPlace;
import com.sarafanka.team.sarafanka.server.repository.*;
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
    private MarketologWorkingPlaceRepository mwpRepo;
    @Autowired
    private BarmenWorkingPlaceRepository bwpRepo;
    @Autowired
    private Services services = new ServicesImpl();


    @RequestMapping(value = "/establisments/getestablishmentbyid", method = RequestMethod.GET)
    @ResponseBody
    public Establishment getEstablishmentsByEstID(@RequestParam(value ="estid",required = true,defaultValue = "") Long estID)
    {
        return estRepository.findByid(estID);
    }
    @RequestMapping(value = "/establisments/getestablishmentbymarketologid", method = RequestMethod.GET)
    @ResponseBody
    public List<Establishment> getEstablishmentsByMarketologId(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID)
    {
        return estRepository.findByCompanyID(marRepository.findByAccountID(accountID).getCompanyID());
    }

    @RequestMapping(value = "/establisments/getestablishmentbycorgid", method = RequestMethod.GET)
    @ResponseBody
    public List<Establishment> getEstablishmentsByCompanyId(@RequestParam(value ="orgid",required = true,defaultValue = "") Long orgID)
    {
        return estRepository.findByCompanyID(orgID);
    }

    @RequestMapping(value = "/establisments/deleteestablishment", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteEstablishments(@RequestParam(value ="estid",required = true,defaultValue = "") Long id)
    {
        estRepository.delete(id);
        for (BarmenWorkingPlace bwp:bwpRepo.findAllByEstablishmentID(id)) {
        bwpRepo.delete(bwp);
        //Если не осталось записи о рабочем месте бармена (Если работал только в удаляемом заведении),
        //Тогда будет работать во всех заведениях
        if (bwpRepo.findAllByBarmenID(bwp.getbarmenID())==null){
            for (Establishment est: estRepository.findByCompanyID(estRepository.findByid(id).getCompanyID())) {
                BarmenWorkingPlace newBWP = new BarmenWorkingPlace();
                newBWP.setbarmenID(bwp.getbarmenID());
                newBWP.setEstablishmentID(est.getId());
                bwpRepo.saveAndFlush(newBWP);
            }
        }
    }

        for (MarketologWorkingPlace mwp:mwpRepo.findAllByEstablishmentID(id)) {
            mwpRepo.delete(mwp);
            //Если не осталось записи о рабочем месте маркетолога (Если работал только в удаляемом заведении),
            //Тогда будет работать во всех заведениях
            if (mwpRepo.findAllByMarketologID(mwp.getMarketologID())==null){
                for (Establishment est: estRepository.findByCompanyID(estRepository.findByid(id).getCompanyID())) {
                    MarketologWorkingPlace newMWP = new MarketologWorkingPlace();
                    newMWP.setMarketologID(mwp.getMarketologID());
                    newMWP.setEstablishmentID(est.getId());
                    mwpRepo.saveAndFlush(newMWP);
                }
            }
        }
        return 1;
    }

    @RequestMapping(value = "/establisments/createEstablishment", method = RequestMethod.GET)
    @ResponseBody
    public Integer createEstablishments(@RequestParam(value ="login",required = true,defaultValue = "") String login,
                                        @RequestParam(value ="adress",required = true,defaultValue = "") String adress,
                                        @RequestParam(value ="estphone",required = true,defaultValue = "") String estPhone)
    {
        return services.createEstablishment(login,adress,estPhone);
    }

    @RequestMapping(value = "/establisments/createEstablishment", method = RequestMethod.POST)
    @ResponseBody
    public Integer createNewEst(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID, @RequestBody Establishment newEst){
        Boolean duplicate = false;
        for (Establishment est: estRepository.findAll()) {
            if (est.getEstName().equals(newEst)){
                duplicate = true;
            }
        }
        if (!duplicate) {
            newEst.setCompanyID(marRepository.findByAccountID(accountID).getCompanyID());
            estRepository.saveAndFlush(newEst);
            return 1;
        }
        else
            return -1;
    }

    @RequestMapping(value = "/establisments/editEstablishment", method = RequestMethod.POST)
    @ResponseBody
    public Integer editEst(@RequestBody Establishment newEst){
        Boolean duplicate = false;
        for (Establishment est: estRepository.findAll()) {
            if (est.getEstName().equals(newEst)){
                duplicate = true;
            }
        }
        if (!duplicate) {
            //Изменение записи в таблице заведений
            estRepository.setNewInfo(newEst.getId(),newEst.getEstName(),newEst.getEstDescription(),newEst.getFactAdress(),
                    newEst.getEstPhone(),newEst.getEstSite(),newEst.getEstEmail(),newEst.getEstWorkTime());
            return 1;
        }
        else
            return -1;
    }
}
