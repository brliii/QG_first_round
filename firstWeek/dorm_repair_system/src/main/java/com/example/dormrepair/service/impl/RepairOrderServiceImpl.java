package com.example.dormrepair.service.impl;

import com.example.dormrepair.constant.OrderStatusConstant;
import com.example.dormrepair.dao.RepairOrderMapper;
import com.example.dormrepair.dbutils.DbUtils;
import com.example.dormrepair.entity.RepairOrder;
import com.example.dormrepair.service.RepairOrderService;
import com.sun.jdi.PathSearchingVirtualMachine;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class RepairOrderServiceImpl implements RepairOrderService {
    //学生创建列表
    @Override
    public boolean createRepairOrder(int userId, String deviceType, String description, int priority) {
        //创建会话
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            RepairOrder order = new RepairOrder();
            order.setUserId(userId);
            order.setDeviceType(deviceType);
            order.setDescription(description);
            order.setPriority(priority);
            order.setStatus(OrderStatusConstant.PENDING);//刚创建保修单，所以是待处理

            int result = mapper.insert(order);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    //学生查看自己的保修单列表
    @Override
    public List<RepairOrder> getOrderByUserId(int userId) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            return mapper.selectByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //学生根据id获取单个保修单
    @Override
    public RepairOrder getOrderById(int orderId) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            return mapper.selectById(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //学生取消保修单
    @Override
    public boolean cancelOrder(int orderId, int userId) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            RepairOrder order = mapper.selectById(orderId);
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
            int result = mapper.updateById(order);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    //管理员查询所有保修单
    @Override
    public List<RepairOrder> getAllOrders() {
        //获取会话
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            //用mp的查询整个列表的用法
            return mapper.selectList(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //管理袁按状态查询保修单
    @Override
    public List<RepairOrder> getOrdersByStatus(int status) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            return mapper.selectByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    //管理员更新保修单状态
    @Override
    public boolean updateOrderStatus(int orderId, int newStatus, int adminId) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            RepairOrder order = mapper.selectById(orderId);
            if (order == null) {
                System.out.println("报修单不存在");
                return false;
            }
            order.setStatus(newStatus);
            order.setAdminId(adminId);
            //update_time 字段在数据库会自动更新
            int result = mapper.updateById(order);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    //管理员删除保修单
    @Override
    public boolean deleteOrder(int orderId) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            int result = mapper.deleteById(orderId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    //管理员按优先级查询保修单
    @Override
    public List<RepairOrder> getOrderByPriority(int priority) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            return mapper.selectByPriority(priority);//mp没有
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //学生评价保修单
    @Override
    public boolean evaluateOrder(int orderId, int userId, int score) {
        SqlSession session = null;
        try {
            session = DbUtils.getSqlSession();
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            RepairOrder order = mapper.selectById(orderId);
            if (order == null) {
                System.out.println("报修单不存在");
                return false;
            }
            if (order.getUserId() != userId) {//只能评价自己的
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
            int result = mapper.updateById(order);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

}