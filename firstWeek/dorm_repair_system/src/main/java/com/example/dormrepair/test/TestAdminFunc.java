package com.example.dormrepair.test;

import com.example.dormrepair.constant.OrderStatusConstant;
import com.example.dormrepair.entity.RepairOrder;
import com.example.dormrepair.service.RepairOrderService;
import com.example.dormrepair.service.impl.RepairOrderServiceImpl;

import java.util.List;

public class TestAdminFunc {
    public static void main(String[] args) {
        RepairOrderService service=new RepairOrderServiceImpl();

        //查询所有报修单
        List<RepairOrder> all = service.getAllOrders();
        System.out.println("所有报修单数量：" + all.size());
        if (all != null) {
            for (int i=0;i<all.size();i++) {
                System.out.println("订单id：" + all.get(i).getId() + "，用户id：" + all.get(i).getUserId() + "，设备类型：" + all.get(i).getDeviceType() + "，状态：" + all.get(i).getStatus());
            }
        }

        //按状态查询
        List<RepairOrder> pending = service.getOrdersByStatus(OrderStatusConstant.PENDING);
        System.out.println("待处理数量：" + pending.size());

        //更新状态
        boolean update = service.updateOrderStatus(1, OrderStatusConstant.PROCESSING, 2);
        System.out.println("更新状态结果：" + update);

        //删除保修单
        boolean delete = service.deleteOrder(1);
        System.out.println("删除结果：" + delete);


    }
}
