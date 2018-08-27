package com.sarafanka.team.sarafanka.server.entity;

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
    @Column(name="factAdress",nullable = false,length = 30)
    private String factAdress;
    @Column(name="estphone",nullable = false,length = 30)
    private String estPhone;

    public String getEstPhone() {
        return estPhone;
    }

    public void setEstPhone(String estPhone) {
        this.estPhone = estPhone;
    }



    public Establishment() {
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
}
