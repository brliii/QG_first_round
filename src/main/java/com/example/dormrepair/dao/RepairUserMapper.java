package com.example.dormrepair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dormrepair.entity.RepairUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface RepairUserMapper extends BaseMapper<RepairUser> {
    //根据用户账号查询用户
    @Select("SELECT * FROM repair_user WHERE username=#{username} ")
    RepairUser selectByUsername(String username);

}