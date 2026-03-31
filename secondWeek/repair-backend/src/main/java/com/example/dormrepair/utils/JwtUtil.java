//工具类，生成 token、解析 token、验证 token
package com.example.dormrepair.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long duration;

    private Key getSigningKey() {//签名密钥
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Integer userId, Integer role) {
        Date now = new Date();
        Date endDate = new Date(now.getTime() + duration);
        return Jwts.builder().claim("userId", userId).claim("role", role).setIssuedAt(now).setExpiration(endDate).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public Claims parseToken(String token) {//？
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public Integer getUserIdFromToken(String token) {
        return parseToken(token).get("userId", Integer.class);
    }

    public Integer getRoleFromToken(String token) {
        return parseToken(token).get("role", Integer.class);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}