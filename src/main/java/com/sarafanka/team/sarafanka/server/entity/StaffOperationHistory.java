package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "staffOperationHistory")

public class StaffOperationHistory {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="staffAccountID",nullable = false,length = 30)
    private Long staffAccountID;
    @Column(name="operationType",nullable = false,length = 150)
    private String operationType;
    @Column(name="operationDate",nullable = false,length = 30)
    private Long operationDate;
    @Column(name="clientAccountID",nullable = false,length = 30)
    private Long clientAccountID;
    @Column(name="actionID",nullable = false,length = 30)
    private Long actionID;

    public StaffOperationHistory() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getStaffAccountID() {
        return staffAccountID;
    }

    public void setStaffAccountID(Long staffAccountID) {
        this.staffAccountID = staffAccountID;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Long operationDate) {
        this.operationDate = operationDate;
    }

    public Long getClientAccountID() {
        return clientAccountID;
    }

    public void setClientAccountID(Long clientAccountID) {
        this.clientAccountID = clientAccountID;
    }

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }
}
