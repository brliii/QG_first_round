package com.example.dormrepair.dao;
//dao层，数据访问对象，负责与数据库进行交互，提供增删改查等操作

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dormrepair.entity.RepairOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface RepairOrderMapper extends BaseMapper<RepairOrder> {
    //根据学生id查询属于他的所有保修单
    @Select("SELECT * FROM repair_order WHERE user_id=#{userId} ")
    List<RepairOrder> selectByUserId(int userId);

    //管理员根据状态查询保修单
    @Select("SELECT * FROM repair_order WHERE status=#{status} ")
    List<RepairOrder> selectByStatus(int status);

    //管理员根据优先级查询保修单
    @Select("SELECT * FROM repair_order WHERE priority=#{priority} ")
    List<RepairOrder> selectByPriority(int priority);

    //同时按状态和优先级查询
    @Select("SELECT * FROM repair_order WHERE status = #{status} AND priority = #{priority}")
    List<RepairOrder> selectByStatusAndPriority(@Param("status") int status,@Param("priority")int priority);
}