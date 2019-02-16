package com.sarafanka.team.sarafanka.server.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "account")

public class Account {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="login",nullable = true,length = 30)
    private String login;
    @Column(name="phoneNumber",nullable = true,length = 15)
    private String phoneNumber;
    @Column(name="password",nullable = false,length = 30)
    private String password;
    @Column(name="accountType",nullable = false,length = 30)
    private String accountType;
    @Column(name="firstName",nullable = false,length = 30)
    private String firstName;
    @Column(name="secondName",nullable = false,length = 30)
    private String secondName;
    @Column(name="pathToAvatar",length = 150)
    private String pathToAvatar;
    @Column(name="creatingDate",nullable = false,length = 50)
    private Long creatingDate;
    @Column(name="avatarChangeDate",length = 50)
    private Long avatarChangeDate;
    //1 - в аккаунт зашли как минимум 1 раз, 0 - в аккаунт ни разу не заходили
    @Column(name="activated",length = 10)
    private Integer activated;
    @Column(name="loginCount",length = 10)
    private Integer loginCount;

    public Account() {
        setActivated(0);
        setLoginCount(0);
    }
    public Account(String email,String pass) {
        setLogin(email);
        setPassword(pass);
        setPhoneNumber("0");
        setAccountType("marketolog");
        setFirstName("Ваше имя");
        setSecondName("Ваша фамилия");
        setPathToAvatar("noavatar");
        Calendar c = Calendar.getInstance();
        setCreatingDate(c.getTimeInMillis());
        setActivated(0);
        setLoginCount(0);
    }

    public Integer getActivated() {
        return activated;
    }

    public void setActivated(Integer activated) {
        this.activated = activated;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Long getCreatingDate() {
        return creatingDate;
    }

    public void setCreatingDate(Long creatingDate) {
        this.creatingDate = creatingDate;
    }

    public Long getAvatarChangeDate() {
        return avatarChangeDate;
    }

    public void setAvatarChangeDate(Long avatarChangeDate) {
        this.avatarChangeDate = avatarChangeDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    public String getPathToAvatar() {
        return pathToAvatar;
    }

    public void setPathToAvatar(String pathToAvatar) {
        this.pathToAvatar = pathToAvatar;
    }
}
