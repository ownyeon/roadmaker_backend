package com.roadmaker.c_kimjongbeom.controller;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.roadmaker.a_common.security.JwtUtil;
import com.roadmaker.c_kimjongbeom.dto.DataDTO2;
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

    private final JwtUtil jwtUtil; // JWT 토큰 관련 유틸리티 클래스
    private final MemberService memberService; // 회원 관련 서비스
    private final AuthenticationManager authenticationManager; // 인증 처리 관리자
    private final MyUserDetailService userDetailsService; // 사용자 상세 정보 서비스


    //========================================
    //로그인 API
    //========================================
    
    @GetMapping("/checklogin")
public ResponseEntity<?> checkLogin(@CookieValue(value = "Authorization", required = false) String authCookie) {
    // 쿠키가 없거나 비어 있으면 로그인이 되어 있지 않은 상태로 처리
    if (authCookie == null || authCookie.isEmpty()) {
        log.info("로그인 상태 아님 - 쿠키가 없습니다.");
        
        // 로그인 페이지로 리디렉션 (예시로 401 응답)
        return ResponseEntity.status(401).body("로그인되지 않았습니다. 로그인 페이지로 이동해주세요.");
    }

    try {
        // "Bearer "로 시작하는 토큰에서 "Bearer "를 제거
        String token = authCookie.startsWith("Bearer ") ? authCookie.substring(7) : authCookie;

        // JWT 토큰 검증
        if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            String username = jwtUtil.extractUsername(token);
            log.info("로그인 상태 확인: 사용자 = {}", username);
            
            // 로그인 상태라면, 그 상태를 200 OK로 응답
            return ResponseEntity.ok("로그인 상태입니다.");
        } else {
            log.info("유효하지 않은 토큰");

            // 유효하지 않은 토큰일 경우 리프레시 토큰을 통한 갱신 시도
            String refreshToken = ""; // 여기서 리프레시 토큰을 얻어야 합니다.
            
            if (jwtUtil.validateRefreshToken(refreshToken)) {
                // 리프레시 토큰이 유효하다면, 새로운 액세스 토큰 발급
                String newAccessToken = jwtUtil.generateAccessTokenFromRefreshToken(refreshToken);
                
                log.info("새로운 액세스 토큰 생성: {}", newAccessToken);
                
                // 새 액세스 토큰을 클라이언트에게 반환
                return ResponseEntity.ok("새로운 액세스 토큰: " + newAccessToken);
            } else {
                log.error("리프레시 토큰도 유효하지 않음");

                // 리프레시 토큰도 유효하지 않다면 401 응답 반환
                return ResponseEntity.status(401).body("유효하지 않은 토큰. 다시 로그인해주세요.");
            }
        }
    } catch (Exception e) {
        // 예외가 발생하면 오류 메시지 반환
        log.error("로그인 확인 실패: {}", e.getMessage());
        return ResponseEntity.status(500).body("내부 서버 오류");
    }
}

@PostMapping("/login")
public LoginDTO login(@RequestBody MembersDTO member, HttpServletResponse response) {
    LoginDTO loginDTO = new LoginDTO(); // 로그인 결과를 담을 DTO 객체
    log.info("로그인 요청: 이메일 = {}", member.getMemEmail());

    try {
        // 이메일과 비밀번호를 사용하여 인증 토큰 생성
        var authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemEmail(), member.getMemSecret());
        
        // 사용자 인증 처리
        authenticationManager.authenticate(authenticationToken);
        log.info("사용자 인증 성공: {}", member.getMemEmail());

        // 인증 성공 후 회원 정보 조회
        MembersDTO authenUser = memberService.getMemberDetail(member.getMemEmail());
        
        if (authenUser != null) {
            // 인증된 사용자 정보로 LoginDTO에 값 설정
            loginDTO.setMemNickname(authenUser.getMemNickname());
            loginDTO.setMemEmail(authenUser.getMemEmail());
            loginDTO.setMemId(authenUser.getMemId());
            loginDTO.setMemRole(authenUser.getMemRole());
            loginDTO.setMemStatus(authenUser.getMemStatus());
        } else {
            throw new Exception("사용자 정보 없음");
        }

        DataDTO2 dataDTO2 = new DataDTO2(
            loginDTO.getMemId(), member.getMemEmail(),
            loginDTO.getMemRole(), loginDTO.getMemNickname(), loginDTO.getMemStatus()
        );

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(dataDTO2);
        loginDTO.setSuccess(true);
        loginDTO.setMessage("로그인 성공");
        loginDTO.setAccessToken(token);

        // 쿠키에 JWT 토큰 저장
        String encodedToken = URLEncoder.encode("Bearer " + token, "UTF-8");
        Cookie cookie = new Cookie("Authorization", encodedToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 실제 서비스에서는 HTTPS 환경에서만 True로 설정해야 합니다.
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1시간 유효
        response.addCookie(cookie);

        log.info("JWT 토큰 생성 완료: {}", token);
    } catch (Exception e) {
        loginDTO.setSuccess(false);
        loginDTO.setMessage("로그인 실패: " + e.getMessage());
        log.error("로그인 실패: {}", e);
    }

    return loginDTO;
}

@PostMapping("/refresh-token")
public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
    String refreshToken = request.getRefreshToken();
    try {
        if (jwtUtil.validateRefreshToken(refreshToken)) {
            String newAccessToken = jwtUtil.generateAccessTokenFromRefreshToken(refreshToken);
            log.info("새로운 액세스 토큰 생성: {}", newAccessToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } else {
            return ResponseEntity.status(401).body("유효하지 않은 리프레시 토큰");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
    }
}

@PostMapping("/logout")
public LoginDTO logout(@RequestHeader("Authorization") String token) {
    LoginDTO loginDTO = new LoginDTO(); 
    try {
        // 토큰 처리, 예를 들어 블랙리스트에 추가하는 로직
        loginDTO.setSuccess(true);
        loginDTO.setMessage("로그아웃 성공");
        log.info("로그아웃 성공: 토큰 = {}", token);
    } catch (Exception e) {
        loginDTO.setSuccess(false);
        loginDTO.setMessage("로그아웃 실패: " + e.getMessage());
    }
    return loginDTO;
}



    //=========================================


/*

    // 로그인 API
    @PostMapping("/login")
    public LoginDTO login(@RequestBody MembersDTO member, HttpServletResponse response) {
        LoginDTO loginDTO = new LoginDTO(); // 로그인 결과를 담을 DTO 객체
        log.info("로그인 요청: 이메일 = {}", member.getMemEmail()); // 로그인 요청을 받았을 때 이메일을 로그로 출력
        log.info(member.getMemEmail());
        try {
            // 인증 토큰 생성 (이메일, 비밀번호)
            var authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemEmail(), member.getMemSecret());
            // 사용자 인증 처리
            authenticationManager.authenticate(authenticationToken);
            log.info("사용자 인증 성공: {}", member.getMemEmail()); // 인증 성공 시 로그

            // 인증 성공 후 회원 정보 조회
            MembersDTO authenUser = memberService.getMemberDetail(member.getMemEmail());

            if(authenUser !=null){
                //인증된 사용자 정보로 LoginDTO에 값 설정
                loginDTO.setMemNickname(authenUser.getMemNickname());
                loginDTO.setMemEmail(authenUser.getMemEmail());
                loginDTO.setMemId(authenUser.getMemId());
                loginDTO.setMemRole(authenUser.getMemRole());
                loginDTO.setMemStatus(authenUser.getMemStatus());
            } else {
                throw new Exception("사용자 정보 없음");
            }
            
            DataDTO2 dataDTO2 = new DataDTO2(
                loginDTO.getMemId(),member.getMemEmail(),
                loginDTO.getMemRole(),loginDTO.getMemNickname(),loginDTO.getMemStatus()
                );
            // JWT 토큰 생성
            String token = jwtUtil.generateToken(dataDTO2);
            log.info("controller nickname 넣기",member.getMemNickname());
            log.info("controller email 넣기",member.getMemEmail());
            log.info("controller id 넣기",member.getMemId());
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



    // 로그인 상태 확인 API(쿠키에서 JWT 토큰 검증)
    @GetMapping("/checklogin")
    public ResponseEntity<?> checkLogin(@CookieValue(value = "Authorization", required = false) String authCookie) {
        // 쿠키에서 Authorization 값 가져오기

        // 쿠키가 없는 경우 또는 쿠키 값이 없으면 로그인되지 않은 상태로 처리
        if (authCookie == null || authCookie.isEmpty()) {
            log.info("쿠키가 없습니다. 로그인 상태 아님");
            return ResponseEntity.status(401).body("로그인되지 않았습니다."); // 401 상태 코드 반환
        }

        try {
            // Bearer 토큰에서 "Bearer " 부분을 제거
            String token = authCookie.startsWith("Bearer ") ? authCookie.substring(7) : authCookie;

            // JWT 토큰 검증
            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                // 토큰이 유효하면 사용자 정보 추출
                String username = jwtUtil.extractUsername(token);
                log.info("로그인 상태: 사용자 = {}", username);

                // 로그인 상태인 경우 200 OK 응답
                return ResponseEntity.ok("로그인 상태입니다.");
            } else {
                log.info("유효하지 않은 토큰");
                return ResponseEntity.status(401).body("유효하지 않은 토큰");
            }
        } catch (Exception e) {
            // 예외 발생 시 에러 로그 출력
            log.error("로그인 확인 실패: {}", e.getMessage());
            return ResponseEntity.status(500).body("내부 서버 오류");
        }
    }
    //==================================================    

*/

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


    /*
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
        */
}
