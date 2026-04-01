package com.example.dormrepair.service;

import com.example.dormrepair.entity.RepairOrder;
import java.util.List;

public interface RepairOrderService {
    //学生创建表单
    boolean createRepairOrder(int userId,String deviceType,String description,int priority,String imageUrl);

    //学生查看自己的保修单列表
    //用list是因为一个学生可能有多个保修单，数量不确定
    List<RepairOrder> getOrderByUserId(int userId);

    //学生查看单个保修单详情
    RepairOrder getOrderById(int orderId);

    //学生取消保修单（注意：仅限于待处理状态）
    boolean cancelOrder(int orderId,int userId);

    //学生对保修单的评价
    boolean evaluateOrder(int orderId, int userId, int score);//1-5星

    //管理员查询所有保修单
    List<RepairOrder> getAllOrders();

    //管理员按状态查询保修单
    List<RepairOrder>getOrdersByStatus(int status);

    //管理员修改保修单状态（注意：要更新update_time）
    boolean updateOrderStatus(int orderId,int newStatus,int adminId);

    //管理员删除保修单
    boolean deleteOrder(int orderId);

    //管理员按优先级查询保修单
    List<RepairOrder> getOrderByPriority(int priority);

    //支持同时按状态和优先级筛选
    List<RepairOrder> getOrdersByStatusAndPriority(int status, int priority);

}
