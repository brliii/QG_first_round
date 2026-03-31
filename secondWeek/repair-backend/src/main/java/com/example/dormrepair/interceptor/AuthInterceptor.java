//拦截器，对除登录/注册外的请求进行 token 验证，并将 userId、role 存入 request 属性
package com.example.dormrepair.interceptor;

import com.example.dormrepair.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class AuthInterceptor implements HandlerInterceptor {//实现Spring的拦截器接口

    @Autowired//自动注入JWT工具类，用于解析和验证token
    private JwtUtil jwtUtil;

    //这是拦截器的核心方法，在控制器方法执行之前调用。返回true则放行，返回false则拦截。
    @Override
    //白名单：登录和注册接口不需要验证token，直接放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //放行预检请求（OPTIONS）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String path = request.getRequestURI();
        if (path.contains("/api/user/login") || path.contains("/api/user/register")) {
            return true;
        }
        //放行静态资源
        if (path.startsWith("/uploads/")) {
            return true;
        }

        //验证 Authorization 头是否存在，并且是否以 "Bearer " 开头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        //提取并验证token
        String token = authHeader.substring(7);//去掉 "Bearer " 前缀，只保留token本身
        if (!jwtUtil.validateToken(token)) {//调用JWT工具类验证token是否有效
            response.setStatus(401);
            return false;
        }

        //从token中解析出用户ID和角色
        Integer userId = jwtUtil.getUserIdFromToken(token);
        Integer role = jwtUtil.getRoleFromToken(token);
        //将这两个信息存入request属性中，后续的控制器方法可以通过 request.getAttribute("userId") 获取当前登录用户信息
        request.setAttribute("userId", userId);//相当于贴标签
        request.setAttribute("role", role);
        return true;
    }
}