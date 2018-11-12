package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Coupon;
import com.sarafanka.team.sarafanka.server.repository.CookieAndSessionRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class MarketologController {
    @Autowired
    private Services services = new ServicesImpl();
    @Autowired
    private CookieAndSessionRepository cookieAndSessionRepository;


    @RequestMapping(value = "/marketolog/getmarketologrole", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Integer> getMarketologRole(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID,
                                                     @RequestHeader("Set-Cookie") String userCookie)
    {
        if (userCookie.equals(cookieAndSessionRepository.findByAccountID(accountID).getCookie()))
        return new ResponseEntity<>(services.getMarketologRole(accountID), HttpStatus.OK);
        else
          return   new  ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
