package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "marketologWorkingPlace")
public class MarketologWorkingPlace {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    @Column(name="marketologID",nullable = false,length = 30)
    private Long marketologID;
    @Column(name="establishmentID",nullable = false,length = 30)
    private Long establishmentID;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getMarketologID() {
        return marketologID;
    }

    public void setMarketologID(Long marketologID) {
        this.marketologID = marketologID;
    }

    public Long getEstablishmentID() {
        return establishmentID;
    }

    public void setEstablishmentID(Long establishmentID) {
        this.establishmentID = establishmentID;
    }

    public MarketologWorkingPlace() {

    }
}
