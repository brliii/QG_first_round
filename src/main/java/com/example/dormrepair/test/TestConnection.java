package com.example.dormrepair.test;  // 根据你实际放的包名调整

import com.example.dormrepair.dao.RepairUserMapper;
import com.example.dormrepair.entity.RepairUser;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class TestConnection {
    public static void main(String[] args) {
        SqlSession sqlSession = null;
        try {//try可以避免异常，可以自动关闭sqlsession，防止资源泄露
            //加载 MyBatis 核心配置文件
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(inputStream);

            //获取 SqlSession
            sqlSession = sqlSessionFactory.openSession();
            System.out.println("Current database: " + sqlSession.getConnection().getCatalog());

            //获取 Mapper 代理对象
            RepairUserMapper mapper = sqlSession.getMapper(RepairUserMapper.class);

            //执行查询（查询所有用户）
            List<RepairUser> users = mapper.selectList(null);
            System.out.println("当前用户数量：" + users.size());
            System.out.println("数据库连接成功！");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}