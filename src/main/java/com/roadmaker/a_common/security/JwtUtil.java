package com.roadmaker.a_common.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.roadmaker.c_kimjongbeom.dto.DataDTO2;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    // HS256 알고리즘에 적합한 비밀 키 자동 생성
    private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // access token의 유효 기간을 주입받음
    @Value("${jwt.access-token-validity}")
    private long expirationTime;

    // refresh token의 유효 기간을 주입받음
    @Value("${jwt.refresh-token-validity}")
    private long refresh;

    // JWT 토큰 생성
    public String generateToken(DataDTO2 dataDTO2) {
        log.info("generateToken 시작: 사용자 인덱스 = {}", dataDTO2.getMemEmail());  // 사용자 인덱스를 로그에 기록


        // 클레임을 추가 (memId 외에도 다른 사용자 정보 추가)
        Map<String, Object> claims = new HashMap<>();
        claims.put("memId", dataDTO2.getMemId());  // memId 추가
        claims.put("memRole", dataDTO2.getMemRole());    // memRole 추가
        claims.put("memNickname", dataDTO2.getMemNickname());  // memNickname 추가
        claims.put("memStatus", dataDTO2.getMemStatus());  // memStatus 추가

        String token = createToken(claims, dataDTO2.getMemEmail());  // 토큰 생성. memEmail을 subject로 사용
        
        log.info("JWTUTIL JWT 토큰 생성 완료: {}", token);  // 생성된 토큰 로그에 기록
        return token;
    }

    // 클레임을 기반으로 JWT 토큰을 생성하는 내부 메서드
    private String createToken(Map<String, Object> claims, String memEmail) {
        log.info("createToken 시작: 사용자명 = {}, 클레임 수 = {}", memEmail, claims.size());  // 사용자명과 클레임 개수 로그에 기록

        String token = Jwts.builder()
            .setClaims(claims)
            .claim("category", "acceessv& refresh")  // 클레임 설정
            .setSubject(memEmail)  // memEmaild을를 주체로 설정(Unique)
            .setIssuedAt(new Date())  // 발급 시간
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 만료 시간 설정
            .signWith(SignatureAlgorithm.HS256, secretKey)  // 서명 알고리즘 및 비밀 키 설정
            .compact();  // 토큰 생성 후 반환

        log.info("토큰 생성 완료: {}", token);  // 생성된 토큰 로그에 기록
        return token;
    }

    // JWT에서 모든 클레임 정보 추출
    public Claims extractAllClaims(String token) {
        log.info("extractAllClaims 시작: 토큰 = {}", token);  // 파싱할 토큰을 로그에 기록
        
        Claims claims = Jwts.parser()  // 토큰을 파싱하여 클레임을 추출
                .setSigningKey(secretKey)  // 서명에 사용된 비밀 키를 설정
                .parseClaimsJws(token)  // JWT 토큰 파싱
                .getBody();  // 클레임을 반환

        log.info("클레임 추출 완료: {}", claims);  // 추출된 클레임 로그에 기록
        return claims;
    }

    // 리프레시 토큰의 유효성 검사
    public boolean validateRefreshToken(String refreshToken) {
        log.info("validateRefreshToken 시작: 리프레시 토큰 = {}", refreshToken);  // 리프레시 토큰 로그에 기록

        boolean expired = isTokenExpired(refreshToken);  // 만료 여부 검사
        if (expired) {
            log.error("리프레시 토큰 만료됨: {}", refreshToken);  // 만료된 경우 로그
            return false;
        }

        log.info("리프레시 토큰 유효: {}", refreshToken);  // 유효한 경우 로그
        return true;
    }

// 리프레시 토큰을 사용해 새로운 액세스 토큰을 생성
public String generateAccessTokenFromRefreshToken(String refreshToken) {
    log.info("generateAccessTokenFromRefreshToken 시작: 리프레시 토큰 = {}", refreshToken);  // 리프레시 토큰 로그에 기록
    
    // 리프레시 토큰에서 사용자명(memEmail) 추출
    String memEmail = extractUsername(refreshToken);

    DataDTO2 dataDTO2 = new DataDTO2(
        0,memEmail,
        "","",0
        );

    // 새로운 액세스 토큰 생성
    String newAccessToken = generateToken(dataDTO2);  // DataDTO2 객체를 사용하여 액세스 토큰 생성
    
    log.info("새로운 액세스 토큰 생성 완료: {}", newAccessToken);  // 새로운 액세스 토큰 로그에 기록
    return newAccessToken;
}

    // JWT 토큰의 만료 여부 확인
    public boolean isTokenExpired(String token) {
        log.info("isTokenExpired 시작: 토큰 = {}", token);  // 만료 여부를 확인할 토큰을 로그에 기록
        
        boolean expired = extractExpiration(token).before(new Date());  // 토큰 만료 시간이 현재 시간보다 이전인지 확인
        log.info("토큰 만료 여부: {}", expired ? "만료됨" : "유효");  // 만료 여부 로그에 기록
        return expired;
    }

    // JWT 토큰에서 만료 시간 추출
    public Date extractExpiration(String token) {
        log.info("extractExpiration 시작: 토큰 = {}", token);  // 만료 시간을 추출할 토큰을 로그에 기록
        
        Date expirationDate = extractAllClaims(token).getExpiration();  // 만료 시간 클레임을 반환
        log.info("만료 시간 추출 완료: {}", expirationDate);  // 추출된 만료 시간 로그에 기록
        return expirationDate;
    }

    // JWT 토큰에서 사용자명(이메일 혹은 ID) 추출
    public String extractUsername(String token) {
        log.info("extractUsername 시작: 토큰 = {}", token);  // 사용자명을 추출할 토큰을 로그에 기록
        
        String memEmail = extractAllClaims(token).getSubject();  // 주체(사용자 정보) 반환
        log.info("사용자명 추출 완료: {}", memEmail);  // 추출된 사용자명 로그에 기록
        return memEmail;
    }

    // JWT 토큰의 유효성 검증
    public boolean validateToken(String token, String username) {
        log.info("validateToken 시작: 토큰 = {}, 사용자명 = {}", token, username);  // 검증할 토큰과 사용자명을 로그에 기록

        boolean isValid = (username.equals(extractUsername(token)) && !isTokenExpired(token));  // 사용자명이 일치하고, 토큰이 만료되지 않았으면 유효한 토큰
        log.info("토큰 유효성 검증 결과: {}", isValid ? "유효" : "유효하지 않음");  // 유효성 검증 결과 로그에 기록
        return isValid;
    }
}
