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
    //статус запущенной акции: 0 - активная, 1 - завершенная успешно, -1 - удалена пользователем, -2 - купон использован
    @Column(name="complited",nullable = false,length = 30)
    private Integer complited;

    @Column(name="pathToQR",length = 150)
    private String pathToQRCode;
    @Column(name="pathToSarafunkaForFriend",length = 150)
    private String pathToSarafunkaForFriend;
    @Column(name="ractStartDate",length = 50)
    private Long ractStatDate;
    @Column(name="repostCountVK",length = 10)
    private Integer repostCountVK;
    @Column(name="repostCountWA",length = 10)
    private Integer repostCountWA;
    @Column(name="repostCountFB",length = 10)
    private Integer repostCountFB;
    @Column(name="repostCountTW",length = 10)
    private Integer repostCountTW;
    @Column(name="repostCountDownload",length = 10)
    private Integer repostCountDownload;
    @Column(name="repostCountAndroid",length = 10)
    private Integer repostCountAndroid;
    @Column(name="pdfUsagesCount",length = 10)
    private Integer pdfUsagesCount;

    public String getPathToSarafunkaForFriend() {
        return pathToSarafunkaForFriend;
    }

    public void setPathToSarafunkaForFriend(String pathToSarafunkaForFriend) {
        this.pathToSarafunkaForFriend = pathToSarafunkaForFriend;
    }

    public RunningActions() {
        setRepostCountAndroid(0);
        setRepostCountDownload(0);
        setRepostCountFB(0);
        setRepostCountVK(0);
        setRepostCountWA(0);
        setRepostCountTW(0);
        setPdfUsagesCount(0);
    }

    public Integer getPdfUsagesCount() {
        return pdfUsagesCount;
    }

    public void setPdfUsagesCount(Integer pdfUsagesCount) {
        this.pdfUsagesCount = pdfUsagesCount;
    }

    public Integer getRepostCountTW() {
        return repostCountTW;
    }

    public void setRepostCountTW(Integer repostCountTW) {
        this.repostCountTW = repostCountTW;
    }

    public Integer getRepostCountVK() {
        return repostCountVK;
    }

    public void setRepostCountVK(Integer repostCountVK) {
        this.repostCountVK = repostCountVK;
    }

    public Integer getRepostCountWA() {
        return repostCountWA;
    }

    public void setRepostCountWA(Integer repostCountWA) {
        this.repostCountWA = repostCountWA;
    }

    public Integer getRepostCountFB() {
        return repostCountFB;
    }

    public void setRepostCountFB(Integer repostCountFB) {
        this.repostCountFB = repostCountFB;
    }

    public Integer getRepostCountDownload() {
        return repostCountDownload;
    }

    public void setRepostCountDownload(Integer repostCountDownload) {
        this.repostCountDownload = repostCountDownload;
    }

    public Integer getRepostCountAndroid() {
        return repostCountAndroid;
    }

    public void setRepostCountAndroid(Integer repostCountAndroid) {
        this.repostCountAndroid = repostCountAndroid;
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
