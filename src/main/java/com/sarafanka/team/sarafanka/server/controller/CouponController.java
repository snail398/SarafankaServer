package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Company;
import com.sarafanka.team.sarafanka.server.entity.Coupon;
import com.sarafanka.team.sarafanka.server.repository.CouponsRepository;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CouponController {
    @Autowired
    private Services services = new ServicesImpl();
    @Autowired
    private CouponsRepository couponsRepo;

    @RequestMapping(value = "/coupons/getcouponsbyuserlogin", method = RequestMethod.GET)
    @ResponseBody
    public List<Coupon> getCouponsByUserLogin(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID)
    {
        return services.getCouponsByUserLogin(accountID);
    }

    @RequestMapping(value = "/coupons/deleteusedcoupon", method = RequestMethod.GET)
    @ResponseBody
    public String deleteUsedCoupon(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID,
                                    @RequestParam(value ="actionid",required = true,defaultValue = "") Long actionID,
                                    @RequestParam(value ="staffid",required = true,defaultValue = "") Long staffID)
    {
        return services.deleteUsedCoupon(accountID,actionID,staffID);
    }

    @RequestMapping(value = "/coupons/getcouponscount", method = RequestMethod.GET)
    @ResponseBody
    public Integer getCouponsCount(@RequestParam(value ="login",required = true,defaultValue = "") String lgn)
    {
        return services.getCouponsCount(lgn);
    }

    @RequestMapping(value = "/coupons/getonecouponslink", method = RequestMethod.GET)
    @ResponseBody
    public String getOneCouponsLing(@RequestParam(value ="accountid",required = true,defaultValue = "") Long accountID,@RequestParam(value ="actionid",required = true,defaultValue = "") Long actionID)
    {
        return couponsRepo.findAllByAccountIDAndActionID(accountID,actionID).get(0).getPathToSarafunka();
    }


}
