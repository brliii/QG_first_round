package com.example.dormrepair.mainmenu;

import com.example.dormrepair.constant.RoleConstant;
import com.example.dormrepair.entity.RepairOrder;
import com.example.dormrepair.entity.RepairUser;
import com.example.dormrepair.service.RepairOrderService;
import com.example.dormrepair.service.UserService;
import com.example.dormrepair.service.impl.RepairOrderServiceImpl;
import com.example.dormrepair.service.impl.UserServiceImpl;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    //此类用于负责显示主菜单和处理用户输入的选择
    private static Scanner sc = new Scanner(System.in);
    private static UserService userService = new UserServiceImpl();
    private static RepairUser currentUser = null;
    private static RepairOrderService orderService = new RepairOrderServiceImpl();

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                //没有就先注册、登录或退出
                showLoginMenu();
            } else {
                if(currentUser.getRole()== RoleConstant.STUDENT){//学生
                    showStudentMenu();
                }else if(currentUser.getRole()== RoleConstant.ADMIN){//管理员
                    showAdminMenu();
                }
            }
        }
    }

    //初始界面
    private static void showLoginMenu() {
        System.out.println("===== 宿舍报修管理系统 =====");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.print("请选择 (1-3): ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                System.out.println("感谢使用，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效输入，请重新选择");
        }
    }

    private static void login(){
        System.out.println("请输入账号：");
        String username = sc.nextLine();

        //密码输入，暗文显示
        //还是……做不到吗
        //String password;
        //Console console = System.console();
        //if(console!=null){
        //    char[] pswd=console.readPassword("请输入密码：");
        //    password = new String(pswd);
        //}else{
            System.out.println("请输入密码：");
            String password = sc.nextLine();
        //}

        RepairUser user= userService.login(username, password);//调用service层的方法
        if(user!=null) {
            currentUser = user;
            System.out.println("登陆成功！欢迎" + user.getName() + "（" + (user.getRole() == RoleConstant.STUDENT ? "学生" : "管理员/维修人员") + ")");
            if(user.getRole()== RoleConstant.STUDENT) {
                System.out.println("宿舍: " + user.getDormBuilding() + " " + user.getDormRoom());
            }
            if (user.getRole() == RoleConstant.STUDENT) {
                //如果是学生，并且没有绑定宿舍，题型他绑定
                if (user.getDormRoom() == null || user.getDormBuilding() == null) {
                    System.out.println("您还没有绑定宿舍，请先绑定宿舍");
                    //调用绑定宿舍的功能，需要宿舍参数
                    bindDorm();
                }
            }
        }else{
            System.out.println("登录失败，请检查用户名和密码");
        }
    }


    private static void register(){
        System.out.println("请选择角色：1-学生 2-管理员/维修人员：（输入之后请回车）");
        int role=sc.nextInt();
        //吞掉回车，以防后序输入出错
        sc.nextLine();
        //boolean roleOK=false;
        //int count=0;
        //while(roleOK==false) {
            //count++;
            //if(count>3){//好像没有发挥作用
              //  System.out.println("您输入错误次数过多，返回主菜单");
               // return;
            //}
            //先判错误情况
            if (role != 1 && role != 2) {
                System.out.println("无效角色，请重新输入");
                //System.out.println("您还剩下"+(3-count)+"次机会");
                //continue;
                return;
            }
            //roleOK=true;

            System.out.println("请输入账号（学号/工号）：");
            String username = sc.nextLine();
            System.out.println("请输入密码：");
            String password = sc.nextLine();
            System.out.println("请确认密码：");
            String confirm = sc.nextLine();
            System.out.println("请输入姓名：");
            String name=sc.nextLine();

            boolean result=userService.register(name,username,password,confirm,role);
            if(result) {
                System.out.println("注册成功！请返回主界面登录");
            }else{
                System.out.println("注册失败，请检查输入（密码不一致/账号格式有误/账号已经存在……）");
            }
        //}
    }

    //绑定宿舍
    private static void bindDorm(){
        System.out.println("请输入宿舍楼栋：");
        String building = sc.nextLine();
        System.out.println("请输入房间号：");
        String room = sc.nextLine();
        //绑定
        boolean successBind = userService.bindDorm(currentUser.getId(), building, room);
        if (successBind) {
            System.out.println("绑定成功！");
            //要更新用户数据
            currentUser.setDormRoom(room);
            currentUser.setDormBuilding(building);
        } else {
            System.out.println("绑定失败，请重试");
        }
    }


    //==================================================================================
    //==================================================================================
    //学生菜单
    private static void showStudentMenu(){
        System.out.println("\n========= 学生菜单 =========");
        System.out.println("1. 绑定/修改宿舍");
        System.out.println("2. 创建报修单");
        System.out.println("3. 查看我的报修记录");
        System.out.println("4. 取消报修单");
        System.out.println("5. 修改密码");
        System.out.println("6. 退出登录");
        System.out.println("7. 评价保修单");
        System.out.print("请选择 (1-6): ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                bindDorm();
                break;
            case "2":
                createRepairOrder();
                break;
            case "3":
                allMyRepairOrders();
                break;
            case "4":
                cancelMyRepairOrder();
                break;
            case "5":
                changePassword();
                break;
            case "6":
                currentUser = null;//把当前用户置空，回到登录界面
                System.out.println("已退出登录");
                break;
            case "7":
                evaluateRepairOrder();
                default:
                System.out.println("无效输入");
        }
    }

    //管理员菜单
    private static void showAdminMenu() {
        System.out.println("\n========= 管理员菜单 =========");
        System.out.println("1. 查看所有报修单");
        System.out.println("2. 查看报修单详情");
        System.out.println("3. 更新报修单状态");
        System.out.println("4. 删除报修单");
        System.out.println("5. 修改密码");
        System.out.println("6. 退出登录");
        System.out.print("请选择 (1-6): ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                allRepairOrders();
                break;
            case "2":
                viewRepairOrderDetail();
                break;
            case "3":
                updateRepairOrderStatus();
                break;
            case "4":
                deleteRepairOrder();
                break;
            case "5":
                changePassword();
                break;
            case "6":
                currentUser = null;
                System.out.println("已退出登录");
                break;
            default:
                System.out.println("无效输入");
        }
    }


    //==================================================================================
    //==================================================================================
    //学生功能实现
    //创建保修单
    private static void createRepairOrder() {
        System.out.println("请输入设备类型：");
        String deviceType = sc.nextLine();
        System.out.println("请输入问题描述：");
        String description = sc.nextLine();
        System.out.println("请选择优先级：1-低 2-中 3-高");
        int priority = sc.nextInt();
        //吞掉回车
        sc.nextLine();

        //调用service
        boolean result = orderService.createRepairOrder(currentUser.getId(), deviceType, description, priority);
        if(result) {
            System.out.println("保修单创建成功");
        }else{
            System.out.println("创建失败");
        }
    }

    //学生查看自己的所有保修记录
    private static void allMyRepairOrders() {
        List<RepairOrder> orders=orderService.getOrderByUserId(currentUser.getId());
        if(orders == null || orders.isEmpty()){
            System.out.println("您没有任何报修记录");
            return;
        }
        System.out.println("您的报修记录：");
        for(int i=0;i<orders.size();i++){
            RepairOrder order=orders.get(i);//List的方法
            System.out.printf("%d.  ID：%d，设备：%s，状态：%s，创建时间：%s%n",i+1,order.getId(),order.getDeviceType(),getStatusText(order.getStatus()),order.getCreateTime());
        }
        System.out.print("请输入保修单ID查看详情，或输入0退出查询：");
        int orderId=sc.nextInt();
        //吞掉回车
        sc.nextLine();
        if(orderId==0){
            return;
        }else{
            viewOrderDetail(orderId);
        }
    }

    //补充上面的查看单个保修单
    private static void viewOrderDetail(int orderId) {
        RepairOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            System.out.println("报修单不存在");
            return;
        }
        // 检查是否属于当前用户
        if (order.getUserId() != currentUser.getId()) {
            System.out.println("无权查看他人报修单");
            return;
        }
        System.out.println("报修单详情：");
        System.out.println("ID: " + order.getId());
        System.out.println("设备类型: " + order.getDeviceType());
        System.out.println("问题描述: " + order.getDescription());
        System.out.println("状态: " + getStatusText(order.getStatus()));//数字转文字
        System.out.println("优先级: " + getPriorityText(order.getPriority()));//数字转文字
        System.out.println("创建时间: " + order.getCreateTime());
        System.out.println("更新时间: " + order.getUpdateTime());
        if(order.getEvaluation()!=null){//注意，返回只是integer类型，如果没有则默认为null
            System.out.println("满意度评分：" + order.getEvaluation() + "星");
        }else{
            System.out.println("满意度评分：尚未评价");
        }
    }
    //取消保修单
    private static void cancelMyRepairOrder() {
        System.out.print("请输入要取消的报修单ID: ");
        int orderId=sc.nextInt();
        //吞掉回车
        sc.nextLine();
        boolean result=orderService.cancelOrder(orderId,currentUser.getId());
        if(result){
            System.out.println("取消成功");
        }else{
            System.out.println("取消失败（可能是单号错误/不属于你的单/状态不允许取消）");
        }
    }

    //两个数字转文字方法
    private static String getStatusText(int status) {
        switch (status) {
            case 0: return "待处理";
            case 1: return "处理中";
            case 2: return "已完成";
            case 3: return "已取消";
            default: return "未知状态";
        }
    }

    private static String getPriorityText(int priority) {
        switch (priority) {
            case 1: return "低";
            case 2: return "中";
            case 3: return "高";
            default: return "未设置优先级";
        }
    }

    //学生评价保修单
    private static void evaluateRepairOrder() {
        System.out.println("请输入要评价的保修单ID: ");
        int orderId = sc.nextInt();
        sc.nextLine();//吞掉回车
        System.out.println("请输入满意度分数（1-5）：");
        int score=sc.nextInt();
        sc.nextLine();//吞掉回车
        boolean result=orderService.evaluateOrder(orderId,currentUser.getId(),score);
        if(result){
            System.out.println("评价成功，感谢反馈！");
        }else{//提示用户错误的可能性
            System.out.println("评价失败（可能是单号错误/不属于你的单/状态不允许评价/分数不合法）");
        }
    }



    //==================================================================================
    //==================================================================================
    //管理员功能实现
    //管理员查看所有保修单
    private static void allRepairOrders() {
        System.out.println("1. 查看全部保修单");
        System.out.println("2. 按状态筛选");
        System.out.println("3. 按优先级筛选");
        System.out.print("请选择: ");
        String option = sc.nextLine();

        List<RepairOrder> orders;
        if (option.equals("2")) {//按状态
            System.out.print("请输入状态代码 0-待处理 1-处理中 2-已完成 3-已取消: ");
            int status = sc.nextInt();
            sc.nextLine(); //吞掉回车
            orders = orderService.getOrdersByStatus(status);//拿到表
            if (orders == null || orders.isEmpty()) {
                System.out.println("没有找到状态为 " + getStatusText(status) + " 的报修单");
                return;
            }
            System.out.println("状态为 " + getStatusText(status) + " 的报修单：");
        }else if(option.equals("3")){//按优先级
            System.out.println("请选择优先级：1-低 2-中 3-高");
            int priority = sc.nextInt();
            sc.nextLine(); //吞掉回车
            orders = orderService.getOrderByPriority(priority);
            if (orders == null || orders.isEmpty()) {
                System.out.println("没有找到优先级为" + getPriorityText(priority) + "的报修单");
                return;
            }
            System.out.println("优先级为" + getPriorityText(priority) + "的报修单：");
        } else {//全选
            orders = orderService.getAllOrders();
            if (orders == null || orders.isEmpty()) {
                System.out.println("暂无保修单");
                return;
            }
            System.out.println("所有保修单：");
        }

        //打印表头(通用表)
        System.out.printf("%s     %s    %s    %s    %s    %s%n",
                "ID", "用户ID", "设备类型", "状态", "优先级", "创建时间");
        for (int i = 0; i < orders.size(); i++) {
            System.out.printf("%s       %s      %s      %s      %s      %s%n%n",
                    orders.get(i).getId(),
                    orders.get(i).getUserId(),
                    orders.get(i).getDeviceType(),
                    getStatusText(orders.get(i).getStatus()),
                    getPriorityText(orders.get(i).getPriority()),
                    orders.get(i).getCreateTime());
        }
    }

    //管理员查看单个保修单
    private static void viewRepairOrderDetail() {
        System.out.println("查看保修单ID：");
        int orderId = sc.nextInt();
        sc.nextLine();//吞掉回车

        RepairOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            System.out.println("保修单不存在");
            return;
        }
        //打印表头
        System.out.printf("%s     %s    %s    %s    %s    %s    %s    %s    %s%n",
                "ID", "用户ID", "设备类型", "问题描述","状态", "优先级", "管理员ID","创建时间","更新时间");
        System.out.printf("%s       %s      %s      %s      %s      %s      %s      %s      %s%n",
                order.getId(),
                order.getUserId(),
                order.getDeviceType(),
                order.getDescription(),
                getStatusText(order.getStatus()),
                getPriorityText(order.getPriority()),
                order.getAdminId(),
                order.getCreateTime(),
                order.getUpdateTime());
    }


    //管理员更新保修单状态
    private static void updateRepairOrderStatus() {
        System.out.print("请输入要修改状态的保修单ID: ");
        int orderId = sc.nextInt();
        sc.nextLine();

        //先查询是否存在
        RepairOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            System.out.println("保修单不存在");
            return;
        }
        //更新状态
        System.out.println("当前状态: " + getStatusText(order.getStatus()));
        System.out.println("请选择新状态:");
        System.out.println("0 - 待处理");
        System.out.println("1 - 处理中");
        System.out.println("2 - 已完成");
        System.out.println("3 - 已取消");
        System.out.print("请输入数字: ");
        int newStatus = sc.nextInt();
        sc.nextLine();

        //不能超出范围
        if (newStatus < 0 || newStatus > 3) {
            System.out.println("您的选择是无效状态");
            return;
        }

        boolean result = orderService.updateOrderStatus(orderId, newStatus, currentUser.getId());
        if (result) {
            System.out.println("状态更新成功");
        } else {
            System.out.println("状态更新失败");
        }
    }

    //管理员删除保修单，因为是管理员删的所以不需要判断是不是某个学生的单子
    private static void deleteRepairOrder() {
        System.out.print("请输入要删除的保修单ID: ");
        int orderId = sc.nextInt();
        sc.nextLine();

        //先检查是否存在
        RepairOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            System.out.println("保修单不存在");
            return;
        }

        //嗯。一般来讲删除应该都需要确认一下的把
        System.out.print("您确认删除报修单 " + orderId + " 吗？确认请输入Y/y，否则输入N/n: ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("y")) {//这里不区分大小写
            boolean result = orderService.deleteOrder(orderId);

            if (result) {
                System.out.println("删除成功");
            }else{
                System.out.println("删除失败");
            }
        }else{
            System.out.println("已取消删除");
        }
    }



    //==================================================================================
    //==================================================================================
    //其他功能（两者都可以用）
    private static void changePassword() {
        System.out.print("请输入旧密码: ");
        String oldPwd = sc.nextLine();
        System.out.print("请输入新密码: ");
        String newPwd = sc.nextLine();
        System.out.print("请确认新密码: ");
        String confirm = sc.nextLine();

        if (!newPwd.equals(confirm)) {
            //跟注册一样，密码要有二次验证
            System.out.println("两次新密码不一致");
            return;
        }

        //调用service
        boolean result = userService.changePassword(currentUser.getId(), oldPwd, newPwd);
        if (result) {
            System.out.println("密码修改成功");
        } else {
            System.out.println("密码修改失败（旧密码错误或用户不存在）");
        }
    }




//好奇怪。。我的控制台输出怎么这么多

}

