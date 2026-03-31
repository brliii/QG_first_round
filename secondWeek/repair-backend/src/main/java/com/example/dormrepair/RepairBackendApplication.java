//springboot启动入口
//启动内嵌的tomcat服务器；根据依赖自动配置spring容器；
//扫描并注册所有的bean；开始接收和处理http请求

package com.example.dormrepair;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.dormrepair.dao")
public class RepairBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepairBackendApplication.class, args);
	}

}
