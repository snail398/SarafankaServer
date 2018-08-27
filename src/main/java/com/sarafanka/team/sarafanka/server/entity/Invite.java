package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "invites")
public class Invite {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="init_user_id",nullable = false,length = 30)
    private Long init_user_id;
    @Column(name="target_user_id",nullable = false,length = 30)
    private Long target_user_id;
    @Column(name="action_id",nullable = false,length = 30)
    private Long action_id;
    @Column(name="inviteDate",nullable = false,length = 50)
    private Long inviteDate;
    //0 - инвайт отправлен, 1 - принят, -1 - отклонен
    @Column(name="responseCode",nullable = false,length = 50)
    private int responseCode;

    public Long getInviteDate() {
        return inviteDate;
    }

    public void setInviteDate(Long inviteDate) {
        this.inviteDate = inviteDate;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getInit_user_id() {
        return init_user_id;
    }

    public void setInit_user_id(Long init_user_id) {
        this.init_user_id = init_user_id;
    }

    public Long getTarget_user_id() {
        return target_user_id;
    }

    public void setTarget_user_id(Long target_user_id) {
        this.target_user_id = target_user_id;
    }

    public Long getAction_id() {
        return action_id;
    }

    public void setAction_id(Long action_id) {
        this.action_id = action_id;
    }
}
