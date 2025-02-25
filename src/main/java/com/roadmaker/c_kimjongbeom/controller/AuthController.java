package com.roadmaker.c_kimjongbeom.controller;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.roadmaker.a_common.security.JwtUtil;
import com.roadmaker.c_kimjongbeom.dto.LoginDTO;
import com.roadmaker.c_kimjongbeom.dto.MembersDTO;
import com.roadmaker.c_kimjongbeom.service.MemberService;
import com.roadmaker.c_kimjongbeom.service.MyUserDetailService;
import com.roadmaker.c_kimjongbeom.dto.TokenRefreshRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api") // "/api"로 시작하는 요청 URL을 처리하는 컨트롤러
@RequiredArgsConstructor // 자동으로 생성자를 생성하여 의존성 주입을 쉽게 할 수 있게 해주는 Lombok 어노테이션
@Slf4j // 로깅을 위한 Lombok 어노테이션
public class AuthController {

    @Autowired
    private final JwtUtil jwtUtil; // JWT 토큰 관련 유틸리티 클래스
    @Autowired
    private final MemberService memberService; // 회원 관련 서비스
    @Autowired
    private final AuthenticationManager authenticationManager; // 인증 처리 관리자
    @Autowired
    private final MyUserDetailService userDetailsService; // 사용자 상세 정보 서비스

    // 로그인 API
    @PostMapping("/login")
    public LoginDTO login(@RequestBody MembersDTO member, HttpServletResponse response) {
        LoginDTO loginDTO = new LoginDTO(); // 로그인 결과를 담을 DTO 객체
        log.info("로그인 요청: 이메일 = {}", member.getMemEmail()); // 로그인 요청을 받았을 때 이메일을 로그로 출력

        try {
            // 인증 토큰 생성 (이메일, 비밀번호)
            var authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemEmail(), member.getMemSecret());
            // 사용자 인증 처리
            authenticationManager.authenticate(authenticationToken);
            log.info("사용자 인증 성공: {}", member.getMemEmail()); // 인증 성공 시 로그

            // 인증 성공 후 JWT 토큰 생성
            String token = jwtUtil.generateToken(member.getMemEmail());  // 필드명 대문자로 수정
            loginDTO.setSuccess(true); // 로그인 성공
            loginDTO.setMessage("로그인 성공"); // 성공 메시지
            loginDTO.setAccessToken(token); // 생성된 액세스 토큰 반환
            log.info("JWT 토큰 생성 완료: {}", token); // 토큰 생성 완료 로그


        // JWT 토큰을 쿠키에 저장
        log.info("controller 쿠키 저장 시작 : ");  // 쿠키 저장 시작을 알리는 로그

        //쿠키에 바로 Bearer를 병합하는 문자를 넣으면 공백 등에 의해 오류가 발생할 수 있음. 그래서, URLEncoder를 사용해야 함.
        String encodedToken = URLEncoder.encode("Bearer "+token, "UTF-8"); // 문자열을  URL 인코딩을 통해서 전송 가능하게 바꿈

        Cookie cookie = new Cookie("Authorization",encodedToken); // 쿠키 생성
        log.info("쿠키 생성 완료: 이름 = {}, 값 = {}", cookie.getName(), cookie.getValue()); 
        // 쿠키 생성이 정상적으로 이루어졌는지 이름과 값을 로깅

        cookie.setHttpOnly(true); // JavaScript로 접근할 수 없도록 설정
        cookie.setSecure(false); // true시 HTTPS에서만 쿠키를 사용할 수 있도록 설정
        cookie.setPath("/"); // 전체 경로에서 쿠키를 사용할 수 있도록 설정
        cookie.setMaxAge(3600); // 쿠키 만료 시간 설정 (1시간)

        response.addCookie(cookie); // 응답에 쿠키 추가
        log.info("응답에 쿠키 추가 완료: 이름 = {}, 값 = {}", cookie.getName(), cookie.getValue()); 
        // 응답에 쿠키가 제대로 추가되었는지 로그

        log.info("쿠키 설정 완료: 이름 = {}, 값 = {}, 만료시간 = {}", cookie.getName(), cookie.getValue(), cookie.getMaxAge()); 
        // 쿠키의 설정이 끝난 후 전체 상태를 로그로 출력


        } catch (Exception e) {
            loginDTO.setSuccess(false); // 로그인 실패
            loginDTO.setMessage("로그인 실패: " + e.getMessage()); // 실패 메시지
            log.error("로그인 실패: {}", e); // 로그인 실패 로그
        }

        return loginDTO; // 로그인 결과를 반환
    }

    // 회원가입 API
    @PostMapping("/signup")
    public LoginDTO signUp(@RequestBody MembersDTO member) {
        LoginDTO loginDTO = new LoginDTO(); // 회원가입 결과를 담을 DTO 객체
        log.info("회원가입 요청: 이메일 = {}", member.getMemEmail()); // 회원가입 요청을 받았을 때 이메일을 로그로 출력

        try {
            // 회원가입 처리 (사용자 정보를 DB에 저장)
            memberService.insertUser(member);
            loginDTO.setSuccess(true); // 회원가입 성공
            loginDTO.setMessage("회원가입 성공"); // 성공 메시지
            log.info("회원가입 성공: 이메일 = {}", member.getMemEmail()); // 회원가입 성공 로그

        } catch (Exception e) {
            loginDTO.setSuccess(false); // 회원가입 실패
            loginDTO.setMessage("회원가입 실패: " + e.getMessage()); // 실패 메시지
            log.error("회원가입 실패: {}", e.getMessage()); // 회원가입 실패 로그
        }

        return loginDTO; // 회원가입 결과를 반환
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public LoginDTO logout(@RequestHeader("Authorization") String token) {
        LoginDTO loginDTO = new LoginDTO(); // 로그아웃 결과를 담을 DTO 객체
        log.info("로그아웃 요청: 토큰 = {}", token); // 로그아웃 요청을 받았을 때 토큰을 로그로 출력

        try {
            // 로그아웃 처리 (토큰 블랙리스트 처리 등)
            loginDTO.setSuccess(true); // 로그아웃 성공
            loginDTO.setMessage("로그아웃 성공"); // 성공 메시지
            log.info("로그아웃 성공: 토큰 = {}", token); // 로그아웃 성공 로그

        } catch (Exception e) {
            loginDTO.setSuccess(false); // 로그아웃 실패
            loginDTO.setMessage("로그아웃 실패: " + e.getMessage()); // 실패 메시지
            log.error("로그아웃 실패: {}", e.getMessage()); // 로그아웃 실패 로그
        }

        return loginDTO; // 로그아웃 결과를 반환
    }


    // 리프레시 토큰을 사용하여 새로운 액세스 토큰 발급
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        log.info("리프레시 토큰 요청: {}", refreshToken); // 리프레시 토큰 요청을 받았을 때 로그로 출력

        try {
            // 리프레시 토큰 검증
            if (jwtUtil.validateRefreshToken(refreshToken)) {
                String newAccessToken = jwtUtil.generateAccessTokenFromRefreshToken(refreshToken);
                log.info("새로운 액세스 토큰 생성: {}", newAccessToken); // 새로운 액세스 토큰 생성 로그
                // Map으로 응답 반환
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            return ResponseEntity.ok(response); // 새 액세스 토큰을 포함한 맵 반환
            } else {
                log.error("잘못된 리프레시 토큰: {}", refreshToken); // 리프레시 토큰이 유효하지 않으면 오류 로그
                return ResponseEntity.status(401).body("Invalid refresh token"); // 401 상태 코드 반환
            }
        } catch (Exception e) {
            log.error("리프레시 토큰 처리 실패: {}", e.getMessage()); // 리프레시 토큰 처리 중 오류 발생 시 로그
            return ResponseEntity.status(500).body("Internal server error"); // 500 상태 코드 반환
        }
    }
}
