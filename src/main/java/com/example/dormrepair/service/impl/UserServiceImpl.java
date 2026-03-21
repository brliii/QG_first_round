package com.example.dormrepair.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dormrepair.constant.RoleConstant;
import com.example.dormrepair.dao.RepairUserMapper;
import com.example.dormrepair.dbutils.DbUtils;
import com.example.dormrepair.entity.RepairUser;
import com.example.dormrepair.service.UserService;
import org.apache.ibatis.session.SqlSession;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements UserService {
    @Override
    public boolean register(String name,String username,String password,String confirmPassword,int role) {
        //注册的时候，密码易班都有二次验证，检验两次密码是否一致
        if (!password.equals(confirmPassword)) {
            System.out.println("两次密码不一致");
            return false;
        }

        //检查学号、工号格式
        //validate方法在后面有定义
        if (!validateUsername(username, role)) {
            System.out.println("用户名格式不正确");
            return false;
        }

        //检查用户名是否已经存在
        try (SqlSession session = DbUtils.getSqlSession()) {
            RepairUserMapper mapper = session.getMapper(RepairUserMapper.class);
            RepairUser existingUser = mapper.selectByUsername(username);
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
            int result = mapper.insert(user);
            return result > 0;
        }
    }

    //用户账号格式检查
    private boolean validateUsername(String username,int role){
        //先判断用户名不为空，且长度至少为前缀长度4以上
        if(username == null || username.length()<=4){
            return false;
        }

        //判断是学生还是管理员，或者都不是
        if(role== RoleConstant.STUDENT){
            //检查前缀3125/3225
            //感觉是苯蛋的写法，硬编码来的
            char c1=username.charAt(0);
            char c2=username.charAt(1);
            char c3=username.charAt(2);
            char c4=username.charAt(3);

            if(!(c1=='3' && c2=='1' && c3=='2' && c4=='5')
            && !(c1=='3' && c2=='2' && c3=='2' && c4=='5')){
                return false;
            }
        }else if(role==RoleConstant.ADMIN){
            //检查前缀0025
            char c1=username.charAt(0);
            char c2=username.charAt(1);
            char c3=username.charAt(2);
            char c4=username.charAt(3);
            if(!(c1=='0' && c2=='0' && c3=='2' && c4=='5')){
                return false;
            }
        }else{
            //错误输入
            //提示都写在main里吧，就不会乱了
            return false;
        }

        //从第五位开始不能是非数字
        for (int i = 4; i < username.length(); i++) {
            char c=username.charAt(i);
            if(c<'0'||c>'9'){
                return false;
            }
        }

        //所有检查都没问题
        return true;
    }

    //登录功能
    @Override
    public RepairUser login(String username,String password){
        SqlSession session=null;
        try{//try可以自动关闭资源
            session=DbUtils.getSqlSession();
            RepairUserMapper mapper=session.getMapper(RepairUserMapper.class);

            //根据用户账号查询用户
            RepairUser user =mapper.selectByUsername(username);
            if(user==null){
                System.out.println("用户名不存在");
                return null;
            }

            //验证密码
            //这个是bcrypt提供的检验方法
            if(BCrypt.checkpw(password,user.getPassword())){
                return user;
            }else{
                System.out.println("密码错误");
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }

    //修改密码功能
    @Override
    public boolean changePassword(int userId,String oldPassword,String newPassword){//其实就是类似注册的密码步骤
        SqlSession session=null;
        try{//try
            session = DbUtils.getSqlSession();
            RepairUserMapper mapper=session.getMapper(RepairUserMapper.class);

            //根据id查询用户
            RepairUser user=mapper.selectById(userId);
            if(user==null){
                System.out.println("用户不存在");
                return false;
            }

            //验证旧密码
            if(!BCrypt.checkpw(oldPassword,user.getPassword())){
                System.out.println("旧密码错误");
                return false;
            }

            //对新密码加密
            String hashedNewPassword=BCrypt.hashpw(newPassword,BCrypt.gensalt());

            //更新密码
            user.setPassword(hashedNewPassword);
            //这个是mp特有的方法，检查更新了没
            int result=mapper.updateById(user);
            return result>0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }

    //通过id查询用户
    @Override
    public RepairUser getUserById(int userId){
        SqlSession session =null;
        try{
            session=DbUtils.getSqlSession();
            RepairUserMapper mapper=session.getMapper(RepairUserMapper.class);
            //直接用mp里的basemapper的selectbyid方法
            return mapper.selectById(userId);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }

    //学生绑定宿舍
    @Override
    public boolean bindDorm(int userId,String building,String room){
        SqlSession session = null;
        try{//避免异常
            session =DbUtils.getSqlSession();
            RepairUserMapper mapper=session.getMapper(RepairUserMapper.class);
            RepairUser user=mapper.selectById(userId);//selectbyid传入主键值

            if(user==null){
                System.out.println("用户不存在");
                return false;
            }
            user.setDormBuilding(building);
            user.setDormRoom(room);
            int result=mapper.updateById(user);//updatebyid传入实体对象
            return result>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }


}
