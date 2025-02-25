package com.roadmaker.a_common.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.roadmaker.c_kimjongbeom.service.MyUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // JwtUtil 객체를 주입 받아 JWT 관련 작업을 처리
    @Autowired
    private JwtUtil jwtUtil;

    // MyUserDetailService 객체를 주입 받아 사용자의 세부 정보를 조회
    @Autowired
    private UserDetailsService userDetailsService;

    // 생성자 주입
    public JwtRequestFilter(JwtUtil jwtUtil, MyUserDetailService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // 요청을 필터링하여 JWT 토큰을 검증하고, 인증을 설정하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // HTTP 요청에서 Authorization 헤더 추출
        String token = request.getHeader("Authorization");
        String username = null;

        log.info("doFilterInternal 시작: Authorization 헤더 = {}", token);  // Authorization 헤더 내용 로그

        // Authorization 헤더가 있고 "Bearer "로 시작하는 경우 토큰을 추출
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // "Bearer "를 제거하여 토큰만 남기기
            username = jwtUtil.extractUsername(token);  // 토큰에서 사용자명(이메일 또는 ID) 추출
            
            log.info("Bearer 토큰 발견: 토큰 = {}, 추출된 사용자명 = {}", token, username);  // 토큰과 추출된 사용자명 로그
        } else {
            log.warn("Authorization 헤더가 없거나 Bearer 형식이 아님");  // Authorization 헤더가 없거나 형식이 맞지 않음
        }

        // 사용자명이 null이 아니고 SecurityContext에 인증 정보가 없다면 인증을 처리
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("사용자명 {} 인증 처리 시작", username);  // 인증 처리 시작 로그

            // 토큰이 유효한지 확인
            if (jwtUtil.validateToken(token, username)) {
                log.info("토큰 유효성 검증 통과: 사용자명 = {}", username);  // 토큰이 유효하면 로그로 출력
                
                // 사용자 정보를 로드
                var userDetails = userDetailsService.loadUserByUsername(username);
                log.info("사용자 정보 로드 완료: {}", userDetails);  // 로드된 사용자 정보 로그

                // 인증 객체 생성
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // 요청 정보 추가
                
                // 인증 정보를 SecurityContext에 설정하여, 이후의 요청 처리에 인증 정보 사용
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("SecurityContext에 인증 정보 설정 완료: {}", authentication);  // 인증 정보 설정 완료 로그
            } else {
                log.warn("토큰 유효성 검증 실패: 사용자명 = {}", username);  // 유효하지 않은 토큰 로그
            }
        } else {
            if (username == null) {
                log.warn("사용자명 추출 실패: Authorization 헤더 또는 토큰에 문제가 있을 수 있음");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.info("이미 인증된 사용자: {}", SecurityContextHolder.getContext().getAuthentication().getName());  // 이미 인증된 사용자 로그
            }
        }

        // 필터 체인에서 다음 필터로 요청을 전달
        chain.doFilter(request, response);
        log.info("doFilterInternal 종료: 요청을 필터 체인으로 전달");  // 필터 체인으로 요청 전달 완료 로그
    }
}
