package com.example.dormrepair.controller;

import com.example.dormrepair.common.Result;
import com.example.dormrepair.dto.LoginRequest;
import com.example.dormrepair.dto.RegisterRequest;
import com.example.dormrepair.entity.RepairUser;
import com.example.dormrepair.service.UserService;
import com.example.dormrepair.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody LoginRequest loginRequest) {
        RepairUser user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            String token=jwtUtil.generateToken(user.getId(), user.getRole());  //重点，登陆的时候带上token！！！生成JWT token，包含用户ID和角色信息！！！
            Map<String , Object>data=new HashMap<>();//打包数据，返回给前端。创建了一个存储键值对的容器，键是字符串，值任意类型
            data.put("token",token);
            data.put("user",user);
            return Result.success(data);
        } else {
            return Result.error(401, "用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody RegisterRequest registerRequest) {
        boolean success = userService.register(
                registerRequest.getName(),
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getConfirmPassword(),
                registerRequest.getRole()
        );
        if (success) {
            return Result.success(true);
        } else {
            return Result.error(400, "注册失败，请检查输入");
        }
    }

    @GetMapping("/info")
    public Result<RepairUser> getUserInfo(@RequestAttribute("userId") Integer userId) {
        RepairUser user = userService.getUserById(userId);
        return Result.success(user);
    }

    @PutMapping("/bindDorm")
    public Result<Boolean> bindDorm(@RequestAttribute("userId") Integer userId,@RequestParam String building,@RequestParam String room){
        boolean success = userService.bindDorm(userId,building,room);
        if (success) {
            return Result.success(true);
        }else{
            return Result.error(400,"绑定失败，用户不存在");
        }
    }

    @PutMapping("/password")
    public Result<Boolean> changePassword(@RequestAttribute("userId") Integer userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        boolean success = userService.changePassword(userId, oldPassword, newPassword);
        if(success){
            return Result.success(true);
        }else{
            return Result.error(400,"修改失败");
        }
    }

}