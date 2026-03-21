package com.example.dormrepair.test;

import com.example.dormrepair.entity.RepairOrder;
import com.example.dormrepair.service.RepairOrderService;
import com.example.dormrepair.service.impl.RepairOrderServiceImpl;

import java.util.List;

public class TestCreateRepairOrder {
    public static void main(String[] args) {
        RepairOrderService repairService=new RepairOrderServiceImpl();
        //创建保修单
        boolean result=repairService.createRepairOrder(1,"空调","不制冷",2);
        System.out.println("创建保修单结果："+(result?"成功":"失败"));

        //查询用户的保修单
        List<RepairOrder> list= repairService.getOrderByUserId(1);
        if(list!=null){
            System.out.println("查询到"+list.size()+"条记录");
            for(int i=0;i<list.size();i++){
                RepairOrder order=list.get(i);
                System.out.println("保修单id："+order.getId()+"，状态："+order.getStatus());
            }
        }else{
            System.out.println("查询失败");
        }

        //查看单个保修单详情
        if(list!=null && !list.isEmpty()) {
            int orderId = list.get(0).getId();//注意这里是从0开始
            RepairOrder detail = repairService.getOrderById(orderId);
            if (detail != null) {
                System.out.println("详情：设备=" + detail.getDeviceType() + "，描述" + detail.getDescription());
            } else {
                System.out.println("获取详情失败");
            }


            //取消保修单
            boolean cancel = repairService.cancelOrder(orderId, 1);
            System.out.println("取消结果：" + cancel);
        }
    }
}
