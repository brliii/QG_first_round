package com.example.dormrepair.service.impl;

import com.example.dormrepair.constant.OrderStatusConstant;
import com.example.dormrepair.dao.RepairOrderMapper;
import com.example.dormrepair.entity.RepairOrder;
import com.example.dormrepair.service.RepairOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepairOrderServiceImpl implements RepairOrderService {

    private final RepairOrderMapper orderMapper;

    //学生创建列表
    @Override
    public boolean createRepairOrder(int userId, String deviceType, String description, int priority,String imageUrl) {
        RepairOrder order = new RepairOrder();
        order.setUserId(userId);
        order.setDeviceType(deviceType);
        order.setDescription(description);
        order.setPriority(priority);
        order.setStatus(OrderStatusConstant.PENDING); // 刚创建保修单，所以是待处理
        order.setImageUrl(imageUrl);

        int result = orderMapper.insert(order);
        return result > 0;
    }

    //学生查看自己的保修单列表
    @Override
    public List<RepairOrder> getOrderByUserId(int userId) {
        return orderMapper.selectByUserId(userId);
    }

    //学生根据id获取单个保修单
    @Override
    public RepairOrder getOrderById(int orderId) {
        return orderMapper.selectById(orderId);
    }

    //学生取消保修单
    @Override
    public boolean cancelOrder(int orderId, int userId) {
        RepairOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            System.out.println("保修单不存在");
            return false;
        }

        //检查是否为null
        if (order.getUserId() == null) {
            System.out.println("保修单数据异常，user_id为空");
            return false;
        }
        //检查保修单单号是否是本人的，否则不可以修改
        if (order.getUserId() != userId) {
            System.out.println("你没有权限取消这个保修单，只能取消自己的保修单");
            return false;
        }
        //如果不是待处理的单子也不能取消哦
        if (order.getStatus() != OrderStatusConstant.PENDING) {
            System.out.println("只能取消待处理状态的保修单，当前状态不允许取消");
            return false;
        }

        order.setStatus(OrderStatusConstant.CANCELLED);
        //mp的updatebyid方法会返回是否更新，是为1，否为0
        int result = orderMapper.updateById(order);
        return result > 0;
    }

    //管理员查询所有保修单
    @Override
    public List<RepairOrder> getAllOrders() {
        //用mp的查询整个列表的用法
        return orderMapper.selectList(null);
    }

    //管理袁按状态查询保修单
    @Override
    public List<RepairOrder> getOrdersByStatus(int status) {
        return orderMapper.selectByStatus(status);
    }

    //管理员更新保修单状态
    @Override
    public boolean updateOrderStatus(int orderId, int newStatus, int adminId) {
        RepairOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            System.out.println("报修单不存在");
            return false;
        }
        order.setStatus(newStatus);
        order.setAdminId(adminId);
        //update_time 字段在数据库会自动更新
        int result = orderMapper.updateById(order);
        return result > 0;
    }

    //管理员删除保修单
    @Override
    public boolean deleteOrder(int orderId) {
        int result = orderMapper.deleteById(orderId);
        return result > 0;
    }

    //管理员按优先级查询保修单
    @Override
    public List<RepairOrder> getOrderByPriority(int priority) {
        return orderMapper.selectByPriority(priority); // mp没有，需要自定义
    }

    //学生评价保修单
    @Override
    public boolean evaluateOrder(int orderId, int userId, int score) {
        RepairOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            System.out.println("报修单不存在");
            return false;
        }
        if (order.getUserId() != userId) { // 只能评价自己的
            System.out.println("您无权评价他人报修单");
            return false;
        }
        if (order.getStatus() != OrderStatusConstant.COMPLETED) {
            System.out.println("只有已完成的报修单才能评价");
            return false;
        }
        if (score < 1 || score > 5) {
            System.out.println("评价分数应在1-5之间");
            return false;
        }
        order.setEvaluation(score);
        int result = orderMapper.updateById(order);
        return result > 0;
    }
}