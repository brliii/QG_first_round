package com.example.dormrepair.service;

import com.example.dormrepair.dao.RepairUserMapper;
import com.example.dormrepair.dbutils.DbUtils;
import com.example.dormrepair.entity.RepairUser;
import org.apache.ibatis.session.SqlSession;

public interface UserService {
    //注册
    boolean register (String name,String username,String password,String confirmPassword,int role);

    //登录
    RepairUser login(String username,String password);

    //修改密码
    boolean changePassword(int userId,String oldPassword,String newPassword);

    //根据ID查询用户
    RepairUser getUserById(int userId);

    //绑定宿舍
    boolean bindDorm(int userId,String building,String room);


}
