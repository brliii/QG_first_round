package com.example.dormrepair.test;

import com.example.dormrepair.service.UserService;
import com.example.dormrepair.service.impl.UserServiceImpl;

public class TestBindDorm {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();
        boolean result=userService.bindDorm(1,"10栋","201");
        System.out.println("绑定宿舍结果："+(result?"成功":"失败"));
    }
}
