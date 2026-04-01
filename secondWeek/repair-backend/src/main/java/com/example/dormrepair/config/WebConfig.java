//配置类，注册拦截器，放行白名单。解决跨域请求问题。
package com.example.dormrepair.config;

import com.example.dormrepair.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/api/user/login", "/api/user/register","/uploads/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:"+ uploadPath +"/");
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("*").allowCredentials(true);
    }
}