package com.example.dormrepair.test;

import com.example.dormrepair.entity.RepairUser;
import com.example.dormrepair.service.impl.UserServiceImpl;

public class TestGetUserById {
    public static void main(String[] args) {
            UserServiceImpl userService=new UserServiceImpl();

        RepairUser userById=userService.getUserById(1);
        if(userById!=null){
            System.out.println("查询用户成功："+userById.getUsername());
        }else{
            System.out.println("查询用户失败");
        }
    }
}
