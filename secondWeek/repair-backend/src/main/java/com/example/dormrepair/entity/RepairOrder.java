package com.example.dormrepair.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class RepairOrder {
    @TableId(type= IdType.AUTO)
    private int id;
    private Integer userId;
    private String deviceType;
    private String description;
    private Integer status;
    private Integer priority;
    private Integer adminId;
    private Date createTime;
    private Date updateTime;
    private Integer evaluation;
    private String imageUrl;

    public RepairOrder() {
    }

    public RepairOrder(int id, Integer userId, String deviceType, String description, Integer status, Integer priority, Integer adminId, Date createTime, Date updateTime,Integer evaluation,String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.deviceType = deviceType;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.adminId = adminId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.evaluation = evaluation;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String device_Type) {
        this.deviceType = device_Type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer admin_id) {
        this.adminId = admin_id;
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

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }

}

