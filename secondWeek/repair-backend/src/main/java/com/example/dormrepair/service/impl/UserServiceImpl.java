package com.example.dormrepair.service.impl;

import com.example.dormrepair.constant.RoleConstant;
import com.example.dormrepair.dao.RepairUserMapper;
import com.example.dormrepair.entity.RepairUser;
import com.example.dormrepair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service//标记这个类是一个Spring管理的服务组件
@RequiredArgsConstructor  // 自动生成包含所有 final 字段的构造器
public class UserServiceImpl implements UserService {

    private final RepairUserMapper userMapper;//这也是依赖注入，可以不用注释@Autowired了，lombok会生成构造器来注入这个依赖。spring移除了手动 SqlSession 管理，保留业务逻辑和注释。

    @Override
    public boolean register(String name, String username, String password, String confirmPassword, int role) {
        //注册的时候，密码易班都有二次验证，检验两次密码是否一致
        if (!password.equals(confirmPassword)) {
            System.out.println("两次密码不一致");
            return false;
        }

        //检查学号、工号格式
        if (!validateUsername(username, role)) {
            System.out.println("用户名格式不正确");
            return false;
        }

        //检查用户名是否已经存在
        RepairUser existingUser = userMapper.selectByUsername(username);
        if (existingUser != null) {
            System.out.println("用户名已存在");
            return false;
        }

        //密码加密(要用一个叫做哈希处理的东西)
        //经过处理后密码是不可逆的
        //这就是bcrypt提供的加密方法
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        //创建用户对象
        RepairUser user = new RepairUser();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole(role);
        user.setName(name);

        //插入数据库中
        int result = userMapper.insert(user);
        return result > 0;
    }

    @Override
    public RepairUser login(String username, String password) {
        //根据用户账号查询用户
        RepairUser user = userMapper.selectByUsername(username);
        if (user == null) {
            System.out.println("用户名不存在");
            return null;
        }

        //这个是bcrypt提供的检验方法
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            System.out.println("密码错误");
            return null;
        }
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        RepairUser user = userMapper.selectById(userId);
        if (user == null) {
            System.out.println("用户不存在");
            return false;
        }

        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            System.out.println("旧密码错误");
            return false;
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedNewPassword);
        //这个是mp特有的方法，检查更新了没
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    public RepairUser getUserById(int userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public boolean bindDorm(int userId, String building, String room) {
        RepairUser user = userMapper.selectById(userId);//selectbyid传入主键值

        if (user == null) {
            System.out.println("用户不存在");
            return false;
        }
        user.setDormBuilding(building);
        user.setDormRoom(room);
        int result = userMapper.updateById(user);//updatebyid传入实体对象
        return result > 0;
    }

    //用户账号格式检查
    private boolean validateUsername(String username, int role) {
        //先判断用户名不为空，且长度至少为前缀长度4以上
        if (username == null || username.length() <= 4) {
            return false;
        }

        if (role == RoleConstant.STUDENT) {
            //检查前缀3125/3225
            //感觉是苯蛋的写法，硬编码来的
            char c1 = username.charAt(0);
            char c2 = username.charAt(1);
            char c3 = username.charAt(2);
            char c4 = username.charAt(3);
            if (!(c1 == '3' && c2 == '1' && c3 == '2' && c4 == '5')
                    && !(c1 == '3' && c2 == '2' && c3 == '2' && c4 == '5')) {
                return false;
            }
        } else if (role == RoleConstant.ADMIN) {
            //检查前缀0025
            char c1 = username.charAt(0);
            char c2 = username.charAt(1);
            char c3 = username.charAt(2);
            char c4 = username.charAt(3);
            if (!(c1 == '0' && c2 == '0' && c3 == '2' && c4 == '5')) {
                return false;
            }
        } else {
            //错误输入
            //提示都写在main里吧，就不会乱了
            return false;
        }

        //从第五位开始不能是非数字
        for (int i = 4; i < username.length(); i++) {
            char c = username.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }
}