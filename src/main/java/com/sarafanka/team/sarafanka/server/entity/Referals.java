package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "referals")

public class Referals {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    @Column(name="accountID",nullable = false,length = 10)
    private Long accountID;
    @Column(name="ractID",nullable = false,length = 10)
    private Long ractID;
    //0 - бонус не получен, //1 - бонус получен
    @Column(name="bonusReceived",nullable = false,length = 10)
    private Integer bonusReceived;
    @Column(name="creatingDate",length = 50)
    private Long creatingDate;
    @Column(name="receivingDate",length = 50)
    private Long receivingDate;


    public Referals() {
        this.bonusReceived=0;
        Calendar c = Calendar.getInstance();
        setCreatingDate(c.getTimeInMillis());
    }

    public Referals(Long accountID, Long ractID) {
        this.accountID = accountID;
        this.ractID = ractID;
        this.bonusReceived=0;
        Calendar c = Calendar.getInstance();
        setCreatingDate(c.getTimeInMillis());
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

    public Long getRactID() {
        return ractID;
    }

    public void setRactID(Long ractID) {
        this.ractID = ractID;
    }

    public Integer getBonusReceived() {
        return bonusReceived;
    }

    public void setBonusReceived(Integer bonusReceived) {
        this.bonusReceived = bonusReceived;
    }
    public Long getCreatingDate() {
        return creatingDate;
    }

    public void setCreatingDate(Long creatingDate) {
        this.creatingDate = creatingDate;
    }

    public Long getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(Long receivingDate) {
        this.receivingDate = receivingDate;
    }

}
