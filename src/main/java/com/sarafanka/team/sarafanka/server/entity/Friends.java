package com.sarafanka.team.sarafanka.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class Friends {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name="user1_id",nullable = false,length = 30)
    private Long user1_id;
    @Column(name="user2_id",nullable = false,length = 30)
    private Long user2_id;
    //0-заявка отправлена , 1 - заявка принята( юзеры - друзья)
    @Column(name="status",nullable = false,length = 30)
    private Integer status;

    public Friends() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(Long user1_id) {
        this.user1_id = user1_id;
    }

    public Long getUser2_id() {
        return user2_id;
    }

    public void setUser2_id(Long user2_id) {
        this.user2_id = user2_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

