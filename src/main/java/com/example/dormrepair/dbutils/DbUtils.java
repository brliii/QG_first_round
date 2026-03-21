package com.example.dormrepair.dbutils;
//数据库工具类，管理mp的sqlsession
//如果没有这个类，每个 Mapper 操作前都需要重复编写初始化代码

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import java.io.InputStream;

public class DbUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {//静态方法快，才能够保证只存在一个
        try {//try能处理异常，确保sqlsession正常关闭，不会泄露数据
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //提供数据库会话对象
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession(true); // true 自动提交事务
    }
}