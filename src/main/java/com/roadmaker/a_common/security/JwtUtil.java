package com.roadmaker.a_common.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token-validity}")
    private long access;
    @Value("${jwt.refresh-token-validity}")
    private long refresh;

    private SecretKey getKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 토큰 생성
    public String generateAccessToken(String userId) {
        return generateToken(userId, access);
    }

    // refresh Token 토큰 생성
    public String generateRefreshToken(String userId) {
        return generateToken(userId, refresh);
    }

    // JWT 검증 및 사용자 ID 추출
    private String generateToken(String userId, long validity) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 받은 토큰으로 모든 정보 반환
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 만료 확인 메서드
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰의 만료 시간을 반환
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // 토큰에서 ID 추출
    public String validateAndExtractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 토큰과 사용자 정보가 일치하는지 검사
    public boolean validateToken(String token, UserDetails userDetails){
        try {
            // 토큰에서 ID 추출
            final String userId = validateAndExtractUserId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
