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

    @Column(name="accountLoginID",nullable = false,length = 30)
    private Long accountLoginID;
    @Column(name="actionTitleID",nullable = false,length = 30)
    private Long actionTitleID;
    @Column(name="percentOfComplete",nullable = false,length = 30)
    private Integer percentOfComplete;
    //статус запущенной акции: 0 - активная, 1 - завершенная успешно, -1 - удалена пользователем
    @Column(name="complited",nullable = false,length = 30)
    private Integer complited;

    @Column(name="pathToQR",length = 150)
    private String pathToQRCode;
    @Column(name="ractStartDate",length = 50)
    private Long ractStatDate;

    public RunningActions() {
    }

    public String getPathToQRCode() {
        return pathToQRCode;
    }

    public void setPathToQRCode(String pathToQRCode) {
        this.pathToQRCode = pathToQRCode;
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

    public Long getAccountLoginID() {
        return accountLoginID;
    }

    public void setAccountLoginID(Long accountLogin) {
        this.accountLoginID = accountLogin;
    }

    public Long getActionTitleID() {
        return actionTitleID;
    }

    public void setActionTitleID(Long actionTitle) {
        this.actionTitleID = actionTitle;
    }

    public Integer getPercentOfComplete() {
        return percentOfComplete;
    }

    public void setPercentOfComplete(Integer percentOfComplete) {
        this.percentOfComplete = percentOfComplete;
    }

    public Integer getComplited() {
        return complited;
    }

    public void setComplited(Integer complited) {
        this.complited = complited;
    }
}
