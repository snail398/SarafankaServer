package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "barmenWorkingPlace")
public class BarmenWorkingPlace {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    @Column(name="barmenID",nullable = false,length = 30)
    private Long barmenID;
    @Column(name="establishmentID",nullable = false,length = 30)
    private Long establishmentID;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getbarmenID() {
        return barmenID;
    }

    public void setbarmenID(Long marketologID) {
        this.barmenID = marketologID;
    }

    public Long getEstablishmentID() {
        return establishmentID;
    }

    public void setEstablishmentID(Long establishmentID) {
        this.establishmentID = establishmentID;
    }

    public BarmenWorkingPlace() {

    }
}
