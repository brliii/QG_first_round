//接收登录请求体,与前段交互
package com.example.dormrepair.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}