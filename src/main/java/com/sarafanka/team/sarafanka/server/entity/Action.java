package com.sarafanka.team.sarafanka.server.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "actions")
public class Action {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="title",nullable = false,length = 30)
    private String title;
    @Column(name="description",nullable = false,length = 120)
    private String description;
    @Column(name="organizationID",nullable = false,length = 30)
    private Long organizationID;
    @Column(name="creatorsID",nullable = false,length = 30)
    private Long creatorsID;
    @Column(name="typeOfAction",nullable = false,length = 30)
    private String typeOfAction;
    @Column(name="reward",nullable = false,length = 80)
    private String reward;
    @Column(name="supportReward",nullable = false,length = 80)
    private String supportReward;
    @Column(name="peopleUsed",nullable = false,length = 30)
    private Integer peopleUsed;
    @Column(name="target",nullable = false,length = 10)
    private Integer target;
    @Column(name="timeStart",nullable = false,length = 30)
    private Long timeStart;
    @Column(name="timeEnd",nullable = false,length = 30)
    private Long timeEnd;
    @Column(name="condition",nullable = true,length = 300)
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getCreatorsID() {
        return creatorsID;
    }

    public void setCreatorsID(Long creatorsID) {
        this.creatorsID = creatorsID;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Action() {
    }

    public Action(String title,String description, Long organizationID, Long creatorsID, String typeOfAction, String reward, String supproReward,Integer peopleUsed, Integer target,Long timeStart,Long timeEnd) {
       this.title=title;
       this.description=description;
       this.organizationID = organizationID;
       this.creatorsID =creatorsID;
       this.typeOfAction=typeOfAction;
       this.reward=reward;
       this.supportReward = supproReward;
       this.peopleUsed = peopleUsed;
       this.target = target;
       this.timeStart =timeStart;
       this.timeEnd = timeEnd;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(Long organization) {
        this.organizationID = organization;
    }


    public String getTypeOfAction() {
        return typeOfAction;
    }

    public void setTypeOfAction(String typeOfAction) {
        this.typeOfAction = typeOfAction;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Integer getPeopleUsed() {
        return peopleUsed;
    }

    public void setPeopleUsed(Integer peopleUsed) {
        this.peopleUsed = peopleUsed;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }


    public String getSupportReward() {
        return supportReward;
    }

    public void setSupportReward(String supportReward) {
        this.supportReward = supportReward;
    }
}
