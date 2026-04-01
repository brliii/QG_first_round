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

    @Override
    //白名单
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
        if (path.startsWith("/uploads/")) {
            return true;
        }

        //验证Authorization头是否存在，并且是否以"Bearer "开头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {//调用JWT工具类验证token是否有效
            response.setStatus(401);
            return false;
        }

        Integer userId = jwtUtil.getUserIdByToken(token);
        Integer role = jwtUtil.getRoleByToken(token);
        //将这两个信息存入request属性中，后续的控制器方法可以通过 request.getAttribute("userId") 获取当前登录用户信息
        request.setAttribute("userId", userId);//相当于贴标签
        request.setAttribute("role", role);
        return true;
    }
}