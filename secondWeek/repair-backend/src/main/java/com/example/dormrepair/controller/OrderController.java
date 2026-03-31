//处理所有与保修订单相关的请求
package com.example.dormrepair.controller;


import com.example.dormrepair.common.Result;
import com.example.dormrepair.constant.RoleConstant;
import com.example.dormrepair.dto.CreateOrderRequest;
import com.example.dormrepair.entity.RepairOrder;
import com.example.dormrepair.service.RepairOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController//返回json数据给前段
@RequestMapping("/api/order")//所有方法公用的路径
@RequiredArgsConstructor//lombork自动生成构造器，依赖注入
public class OrderController {

    private final RepairOrderService orderService;

    @Value("${upload.path}")//住入值
    private String uploadPath;

    @PostMapping("/create")
    public Result<Boolean> createOrder(@RequestAttribute("userId") Integer userId,//看标签，然后塞到参数里
                                       @ModelAttribute CreateOrderRequest request) {//这里用了requestbody注释报错了，没法接收文件，所以改成了modelattribute
        //图片上传
        String imageUrl = null;
        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {//因为可以不传图面，所以有图才处理
            try {
                //村图片
                String originalFilename = image.getOriginalFilename();
                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + ext;//要这么做
                //String fileName =image.getOriginalFilename();这样写不行，会导致覆盖
                File dest = new File(uploadPath + File.separator + fileName);

                if (!dest.getParentFile().exists()) {//目录不存在就创建目录
                    dest.getParentFile().mkdirs();
                }
                image.transferTo(dest);//保存图片
                imageUrl = "/uploads/" + fileName; //前端访问路径
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error(500, "图片上传失败");
            }
        }

        boolean success = orderService.createRepairOrder(userId, request.getDeviceType(), request.getDescription(), request.getPriority(), imageUrl);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error(400, "创建报修单失败");
        }
    }

    @GetMapping("/user/orders")
    public Result<List<RepairOrder>> getUserOrders(@RequestAttribute("userId") Integer userId){
        List<RepairOrder> orders=orderService.getOrderByUserId(userId);
        return Result.success(orders);
    }

    @PutMapping("/{orderId}/cancel")
    public Result<Boolean> cancelOrder(@PathVariable int orderId,@RequestAttribute("userId") Integer userId){
        boolean success =orderService.cancelOrder(orderId,userId);
        if(success){
            return Result.success(true);
        }else{
            return Result.error(400,"取消失败，可能是订单状态或个人权限不满足");
        }
    }

    @GetMapping("/admin/list")
    public Result<List<RepairOrder>> getAllOrders(@RequestParam(required=false)Integer status,@RequestAttribute("role") Integer role){
        //管理员才能反问
        if(role==null || role!= RoleConstant.ADMIN){
            return Result.error(403,"无权限访问");
        }
        List<RepairOrder> orders;
        if(status!=null){
            orders=orderService.getOrdersByStatus(status);
        }else{
            orders=orderService.getAllOrders();
        }
        return Result.success(orders);
    }

    //这个是看保修单详情的，详情，跟上面的不一样
    @GetMapping("/{orderId}")
    public Result<RepairOrder> getOrderDetail(@PathVariable int orderId,@RequestAttribute("userId") Integer userId,@RequestAttribute("role") Integer role){
        RepairOrder order =orderService.getOrderById(orderId);
        if(order==null){
            return Result.error(404,"保修单不存在");
        }
        if(role==RoleConstant.ADMIN ||order.getUserId().equals(userId)){
            return Result.success(order);
        }else{
            return Result.error(403,"无权查看");
        }
    }

    @PutMapping("/admin/status/{orderId}")
    public Result<Boolean> updateOrderStatus(@PathVariable int orderId,@RequestParam int newStatus,@RequestAttribute("userId") Integer adminId,@RequestAttribute("role")Integer role){
        if(role==null || role!=RoleConstant.ADMIN){
            return Result.error(403,"无权限");
        }
        boolean success=orderService.updateOrderStatus(orderId,newStatus,adminId);
        if(success){
            return Result.success(true);
        }else{
            return Result.error(400,"修改失败");
        }
    }

    @DeleteMapping("/admin/{orderId}")
    public Result<Boolean> deleteOrder(@PathVariable int orderId,@RequestAttribute("role") Integer role){
        if(role==null || role!=RoleConstant.ADMIN){
            return Result.error(403,"无权限");
        }
        boolean success=orderService.deleteOrder(orderId);
        if(success){
            return Result.success(true);
        }else{
            return Result.error(400,"删除失败");
        }
    }

}
