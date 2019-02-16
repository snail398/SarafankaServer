package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "establishment")
public class Establishment {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="companyID",nullable = false,length = 30)
    private Long companyID;
    @Column(name="factAdress",nullable = false,length = 120)
    private String factAdress;
    @Column(name="estname",nullable = false,length = 70 )
    private String estName;
    @Column(name="estemail",nullable = false,length = 50 )
    private String estEmail;
    @Column(name="estSite",nullable = false,length = 50 )
    private String estSite;
    @Column(name="estdescription",nullable = false,length = 300 )
    private String estDescription;
    @Column(name="estphone",nullable = false,length = 30)
    private String estPhone;
    @Column(name="estworktime",nullable = false,length = 300 )
    private String estWorkTime;
    @Column(name="pathToAvatar",length = 150)
    private String pathToAvatar;
    @Column(name="avatarChangeDate",length = 50)
    private Long avatarChangeDate;
    public Establishment() {
    }

    public Establishment(String name) {
        this.estName = name;

    }
    public Establishment(Long companyID,String companyName) {
        setEstName(companyName);
        setCompanyID(companyID);
        setEstDescription("description");
        setEstEmail("email");
        setEstPhone("231");
        setEstSite("site.com");
        setEstWorkTime("Время работы");
        setFactAdress("address");
        setPathToAvatar("noavatar");


    }
    public String getEstPhone() {
        return estPhone;
    }

    public void setEstPhone(String estPhone) {
        this.estPhone = estPhone;
    }


    public String getPathToAvatar() {
        return pathToAvatar;
    }

    public void setPathToAvatar(String pathToAvatar) {
        this.pathToAvatar = pathToAvatar;
    }

    public Long getAvatarChangeDate() {
        return avatarChangeDate;
    }

    public void setAvatarChangeDate(Long avatarChangeDate) {
        this.avatarChangeDate = avatarChangeDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }

    public String getFactAdress() {
        return factAdress;
    }

    public void setFactAdress(String factAdress) {
        this.factAdress = factAdress;
    }

    public String getEstName() {
        return estName;
    }

    public void setEstName(String estName) {
        this.estName = estName;
    }

    public String getEstEmail() {
        return estEmail;
    }

    public void setEstEmail(String estEmail) {
        this.estEmail = estEmail;
    }

    public String getEstSite() {
        return estSite;
    }

    public void setEstSite(String estSite) {
        this.estSite = estSite;
    }

    public String getEstDescription() {
        return estDescription;
    }

    public void setEstDescription(String estDescription) {
        this.estDescription = estDescription;
    }

    public String getEstWorkTime() {
        return estWorkTime;
    }

    public void setEstWorkTime(String estWorkTime) {
        this.estWorkTime = estWorkTime;
    }
}
