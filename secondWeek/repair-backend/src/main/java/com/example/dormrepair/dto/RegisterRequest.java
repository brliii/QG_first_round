//接收注册请求体
package com.example.dormrepair.dto;  // 或者 com.example.dormrepair.dto

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    private int role;
}