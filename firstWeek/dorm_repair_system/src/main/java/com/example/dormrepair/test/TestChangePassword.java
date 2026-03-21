package com.example.dormrepair.test;

import com.example.dormrepair.entity.RepairUser;
import com.example.dormrepair.service.impl.UserServiceImpl;

public class TestChangePassword {
    public static void main(String[] args) {
        UserServiceImpl userService=new UserServiceImpl();

        //设定一些测试数据
        int userId=1;
        String oldPassword="123456";
        String newPassword="newpassword123";

        //修改密码
        boolean result=userService.changePassword(userId,oldPassword,newPassword);
        System.out.println("修改密码结果："+(result?"成功":"失败"));

        //用新密码登录验证一下
        RepairUser user=userService.login("3125004123",newPassword);
        if(user!=null){
            System.out.println("新密码登录成功，修改密码成功");
        }else{
            System.out.println("新密码登录失败，修改密码失败");
        }

        //en?好像可以用旧密码来测试是否失败
        RepairUser userOld=userService.login("3125004123",oldPassword);
        if(userOld!=null){
            System.out.println("旧密码登录成功，说明修改密码失败");
        }else{
            System.out.println("旧密码登录失败，说明修改密码成功");
        }


    }
}
