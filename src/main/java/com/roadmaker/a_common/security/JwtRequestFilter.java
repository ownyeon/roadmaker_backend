package com.roadmaker.a_common.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 한 요청당 한번만 실행되는 것을 보장하는 필터
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    // @Autowired
    // private UserDetailsService userDetailsService;

    @Override
    // http로 요청이 오면 해당 메서드를 거치게 되어있다.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtRequestFilter 호출");
        final String authorizationHeader = request.getHeader("Authorization");

        String userId = null;
        String jwtToken = null;

        // Bearer 토큰이 있는 경우

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                // 토큰 만료 검사
                if (jwtUtil.isTokenExpired(jwtToken)) {
                    log.info("토큰 만료");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰만료");
                    return;
                }
                userId = jwtUtil.validateAndExtractUserId(jwtToken);

            } catch (Exception e) {
                log.info("JWT 토큰 처리중 오류 발생:" + e.toString());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 처리 오류");
            }
        } else {
            log.info("Authorization NULL 또는 Bearer 토큰이 아닙니다.");
        }

        // 사용자 ID가 존재하고 SecurityContext에 인증 정보가 없는 경우
        // if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        //     // DB에서 사용자 정보 가져오기
        //     // UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
        //     // 사용자와 토큰과 일치하는지 확인.
        //     if (jwtUtil.validateToken(jwtToken, userDetails)) {
        //         UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
        //                 null, userDetails.getAuthorities());

        //         SecurityContextHolder.getContext().setAuthentication(authToken);
        //         log.info("JWT 인증 성공 : {}", userId);
        //     } else {
        //         log.warn("JWT 토큰이 유효하지 않습니다.");
        //     }
        // }

        filterChain.doFilter(request, response);

    }
}
