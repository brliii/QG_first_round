package com.example.dormrepair.constant;

public class OrderStatusConstant {
    //保修单状态
    public static final int PENDING=0;//待处理
    public static final int PROCESSING=1;//处理中
    public static final int COMPLETED=2;//以完成
    public static final int CANCELLED=3;//以取消

    //私有化构造
    private OrderStatusConstant(){}
}
