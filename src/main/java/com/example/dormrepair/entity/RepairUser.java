package com.example.dormrepair.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;


public class RepairUser {
    //wk了，居然还需要在这设置自增
    //怪不得我查不到id1号的
    @TableId(type= IdType.AUTO)
    private int id;
    private String username;//学号工号
    private String password;
    private Integer role;
    private String name;
    //居然因为没有驼峰命名导致映射失败。。下次记得驼峰命名
    private String dormBuilding;
    private String dormRoom;
    private Date createTime;
    private Date updateTime;

    public RepairUser() {
    }

    public RepairUser(int id, String username, String password, Integer role, String name, String dormBuilding, String dormRoom, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.dormBuilding = dormBuilding;
        this.dormRoom = dormRoom;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDormBuilding() {
        return dormBuilding;
    }

    public void setDormBuilding(String dorm_building) {
        this.dormBuilding = dorm_building;
    }

    public String getDormRoom() {
        return dormRoom;
    }

    public void setDormRoom(String dorm_room) {
        this.dormRoom = dorm_room;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date create_time) {
        this.createTime = create_time;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date update_time) {
        this.updateTime = update_time;
    }

}
