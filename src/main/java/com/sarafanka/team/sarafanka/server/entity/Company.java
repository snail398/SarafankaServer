package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="title",nullable = false,length = 70)
    private String title;
    @Column(name="adress",nullable = false,length = 120)
    private String adress;
    @Column(name="companyType",nullable = false,length = 120)
    private String companyType;
    @Column(name="category",nullable = false,length = 120)
    private String category;
    @Column(name="description",nullable = false,length = 300)
    private String description;
    @Column(name="phone",nullable = false,length = 120)
    private String phone;
    @Column(name="site",nullable = false,length = 120)
    private String site;
    @Column(name="pathToAvatar",length = 150)
    private String pathToAvatar;
    @Column(name="inn",length = 150)
    private String inn;
    @Column(name="kpp",length = 150)
    private String kpp;
    @Column(name="ogrn",length = 150)
    private String ogrn;
    @Column(name="creatingDate",nullable = false,length = 50)
    private Long creatingDate;
    @Column(name="avatarChangeDate",length = 50)
    private Long avatarChangeDate;

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getPathToAvatar() {
        return pathToAvatar;
    }

    public void setPathToAvatar(String pathToAvatar) {
        this.pathToAvatar = pathToAvatar;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }


    public Company() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
