package com.example.dormrepair.test;

import com.example.dormrepair.constant.RoleConstant;
import com.example.dormrepair.service.UserService;
import com.example.dormrepair.service.impl.UserServiceImpl;
import com.sun.jdi.PathSearchingVirtualMachine;

public class TestRegister {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        //测试学生注册
        boolean result1=userService.register("张三","3125004123","123456","123456", RoleConstant.STUDENT);
        System.out.println("学生注册结果："+(result1?"成功":"失败"));

        //测试管理员注册
        boolean result2=userService.register("李四","0025004123","admin123","admin123", RoleConstant.ADMIN);
        System.out.println("管理员注册结果："+(result2?"成功":"失败"));

        //测试重复注册
        boolean result3=userService.register("王五","3125004123","123456","123456",RoleConstant.STUDENT);
        System.out.println("重复注册结果："+(result3?"成功":"失败"));

    }
}
