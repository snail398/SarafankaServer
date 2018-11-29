package com.sarafanka.team.sarafanka.server.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="accountID",nullable = false,length = 30)
    private Long accountID;
    @Column(name="companyID",nullable = false,length = 30)
    private Long companyID;
    @Column(name="actionID",nullable = false,length = 30)
    private Long actionID;
    @Column(name="reward",nullable = false,length = 30)
    private String reward;
    @Column(name="pathToQR",length = 150)
    private String pathToQRCode;
    @Column(name="pathToSarafunka",length = 150)
    private String pathToSarafunka;
    @Column(name="ractStartDate",length = 50)
    private Long ractStatDate;

    public Coupon() {
    }

    public String getPathToQRCode() {
        return pathToQRCode;
    }

    public void setPathToQRCode(String pathToQRCode) {
        this.pathToQRCode = pathToQRCode;
    }

    public String getPathToSarafunka() {
        return pathToSarafunka;
    }

    public void setPathToSarafunka(String pathToSarafunka) {
        this.pathToSarafunka = pathToSarafunka;
    }

    public Long getRactStatDate() {
        return ractStatDate;
    }

    public void setRactStatDate(Long ractStatDate) {
        this.ractStatDate = ractStatDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
