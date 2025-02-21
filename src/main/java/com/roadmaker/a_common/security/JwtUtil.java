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
    public String generateAccessToken(String memEmail) {
        System.out.println("jwtutil access토큰생성");
        return generateToken(memEmail, access);
    }

    // refresh Token 토큰 생성
    public String generateRefreshToken(String memEmail) {
        System.out.println("jwtutil refresh토큰생성");
        return generateToken(memEmail, refresh);
    }

    // JWT 검증 및 사용자 ID 추출
    private String generateToken(String memEmail, long validity) {
        
        System.out.println("jwtutil 검증 및 사용자 아이디 추출");
        return Jwts.builder()
                .setSubject(memEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 받은 토큰으로 모든 정보 반환
    public Claims extractAllClaims(String token) {
        
        System.out.println("jwtutil 받은 토큰으로 정보 반환");
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 만료 확인 메서드
    public boolean isTokenExpired(String token) {
        
        System.out.println("jwtutil 토큰 만료 확인");
        return extractExpiration(token).before(new Date());
    }

    // 토큰의 만료 시간을 반환
    public Date extractExpiration(String token) {
        
        System.out.println("jwtutil 토큰 만료 시간 반환");
        return extractAllClaims(token).getExpiration();
    }

    // 토큰에서 ID 추출
    public String validateAndExtractmemEmail(String token) {
        
        System.out.println("jwtutil 토큰 아이디 추출");
        return extractAllClaims(token).getSubject();
    }

    // 토큰과 사용자 정보가 일치하는지 검사
    public boolean validateToken(String token, UserDetails userDetails){
        try {
            // 토큰에서 memEmail(ID) 추출
            final String memEmail = validateAndExtractmemEmail(token);
            // 토큰에서 추출한 이메일과 UserDetails의 이메일이 일치하는지 확인
            System.out.println("토큰과 사용자 정보 비교");
            return (memEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}
