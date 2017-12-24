package com.sarafanka.team.sarafanka.server.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "runningActions")
public class RunningActions {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="accountLogin",nullable = false,length = 30)
    private String accountLogin;
    @Column(name="actionTitle",nullable = false,length = 30)
    private String actionTitle;
    @Column(name="percentOfComplete",nullable = false,length = 30)
    private Integer percentOfComplete;

    public RunningActions() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountLogin() {
        return accountLogin;
    }

    public void setAccountLogin(String accountLogin) {
        this.accountLogin = accountLogin;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public Integer getPercentOfComplete() {
        return percentOfComplete;
    }

    public void setPercentOfComplete(Integer percentOfComplete) {
        this.percentOfComplete = percentOfComplete;
    }
}
