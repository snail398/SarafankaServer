package com.sarafanka.team.sarafanka.server.controller;

import com.sarafanka.team.sarafanka.server.entity.Company;
import com.sarafanka.team.sarafanka.server.entity.Coupon;
import com.sarafanka.team.sarafanka.server.service.Services;
import com.sarafanka.team.sarafanka.server.service.ServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CouponController {
    @Autowired
    private Services services = new ServicesImpl();

    @RequestMapping(value = "/coupons/getcouponsbyuserlogin", method = RequestMethod.GET)
    @ResponseBody
    public List<Coupon> getCouponsByUserLogin(@RequestParam(value ="login",required = true,defaultValue = "") String lgn)
    {
        return services.getCouponsByUserLogin(lgn);
    }

    @RequestMapping(value = "/coupons/deleteusedcoupon", method = RequestMethod.GET)
    @ResponseBody
    public Integer deleteUsedCoupon(@RequestParam(value ="login",required = true,defaultValue = "") String lgn,
                                    @RequestParam(value ="action",required = true,defaultValue = "") Long actionID,
                                    @RequestParam(value ="barmenlogin",required = true,defaultValue = "") String barmenLgn)
    {
        return services.deleteUsedCoupon(lgn,actionID,barmenLgn);
    }

    @RequestMapping(value = "/coupons/getcouponscount", method = RequestMethod.GET)
    @ResponseBody
    public Integer getCouponsCount(@RequestParam(value ="login",required = true,defaultValue = "") String lgn)
    {
        return services.getCouponsCount(lgn);
    }


}
