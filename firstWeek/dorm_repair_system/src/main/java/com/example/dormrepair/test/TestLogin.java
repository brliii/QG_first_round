package com.example.dormrepair.test;

import com.example.dormrepair.entity.RepairUser;
import com.example.dormrepair.service.UserService;
import com.example.dormrepair.service.impl.UserServiceImpl;

public class TestLogin {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        //测试正确的学生登录
        RepairUser user1=userService.login("3125004123","123456");
        if(user1!=null){
            System.out.println("学生登录成功，用户id："+user1.getId()+"，角色："+user1.getRole());
        }else{
            System.out.println("学生登录失败");
        }

        //测试正确的管理员登录
        RepairUser user2=userService.login("0025004123","admin123");
        if(user2!=null){
            System.out.println("管理员登录成功，用户id："+user2.getId()+"，角色："+user2.getRole());
        }else{
            System.out.println("管理员登录失败");
        }

        //测试错误的密码
        RepairUser user3=userService.login("3125004123","wrongpassword");
        if(user3!=null){
            System.out.println("密码错误了居然也能登录成功，说明测试失败");
        }else{
            System.out.println("密码错误登录失败，测试成功");
        }

        //测试不存在的用户名
        RepairUser user4=userService.login("3125009999","123456");
        if(user4!=null){
            System.out.println("用户名不存在了居然也能登录成功，说明测试失败");
        }else{
            System.out.println("用户名不存在登录失败，测试成功");
        }

    }
}
