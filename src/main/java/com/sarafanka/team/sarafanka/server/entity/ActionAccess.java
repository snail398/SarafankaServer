package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "actionAccess")
public class ActionAccess {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    @Column(name="actionID",nullable = false,length = 30)
    private Long actionID;
    @Column(name="establishmentID",nullable = false,length = 30)
    private Long establishmentID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public Long getEstablishmentID() {
        return establishmentID;
    }

    public void setEstablishmentID(Long establishmentID) {
        this.establishmentID = establishmentID;
    }

    public ActionAccess() {

    }
}
