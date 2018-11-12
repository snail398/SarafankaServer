package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "cookieandsession")
public class CookieAndSession {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="accountid",nullable = true,length = 50)
    private Long accountID;
    @Column(name="cookie",nullable = true,length = 300)
    private String cookie;
    @Column(name="session",nullable = true,length = 200)
    private String session;

    public CookieAndSession() {
    }


    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
