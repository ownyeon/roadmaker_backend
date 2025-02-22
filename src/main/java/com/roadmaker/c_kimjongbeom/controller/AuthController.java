package com.roadmaker.c_kimjongbeom.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.a_common.security.JwtUtil;
import com.roadmaker.c_kimjongbeom.dto.MembersDTO;
import com.roadmaker.c_kimjongbeom.service.MemberService;
import com.roadmaker.c_kimjongbeom.service.MyUserDetailService;
import com.roadmaker.f_hwangjinsang.dto.DataDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    //필드 주입, 변경 가능
    @Autowired
    private MemberService memberService;

    //생성자 주입, 변경 불가능
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MyUserDetailService userDetailService;


    //로그인
    /*
        실행 결과값들을 전달할 DataDTO를 활용하여 검증 결과를 전달한다.
        DataDTO는 DB로 유지하지 않는다.
     */

    @PostMapping("login")
    public DataDTO login(@RequestBody MembersDTO mdto, HttpServletResponse response) {

        System.out.println("로그인 컨트롤러 진입");
        DataDTO dataDTO = new DataDTO();

        try {
            // 사용자 정보 확인
            System.out.println("로그인 컨트롤러 사용자 정보 확인");
            UserDetails userDetails = userDetailService.loadUserByUsername(mdto.getMemEmail());
            System.out.println("로그인 컨트롤러 사용자 정보 결과");
            System.out.println("userDetails : " + userDetails);

            // Password가 일치하는지 확인
            System.out.println("로그인 컨트롤러 사용자 비밀번호 확인");
            System.out.println("backend 수신 비밀번호 : "+mdto.getMemSecret());
            System.out.println("backend 수신 비밀번호 : "+userDetails.getPassword());
            System.out.println(" 진위여부 "+passwordEncoder.matches(mdto.getMemSecret().trim(), userDetails.getPassword().trim()));
            if (!passwordEncoder.matches(mdto.getMemSecret().trim(), userDetails.getPassword().trim())) {

                System.out.println("로그인 컨트롤러 사용자 비밀번호 확인 결과");
        System.out.println("비밀번호 불일치 컨트롤러 아웃");
                return new DataDTO(false, null, "비밀번호가 일치하지 않습니다.");
            }
            // jwt 생성
            String refreshToken = jwtUtil.generateRefreshToken(mdto.getMemEmail());
            String accessToken = jwtUtil.generateAccessToken(mdto.getMemEmail());

            // refreshToken DB에 저장
            memberService.saveRefreshToken(userDetails.getUsername(), refreshToken,
                    jwtUtil.extractExpiration(refreshToken));

            // 액세스 토큰을 쿠키에 설정
            Cookie accessTokenCookie = new Cookie("access_token", accessToken);
            accessTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가
            accessTokenCookie.setMaxAge(3600);   // 1시간 유효
            accessTokenCookie.setPath("/");      // 모든 경로에서 접근 가능
            response.addCookie(accessTokenCookie);


            Map<String, String> tokens = new HashMap<>();
            tokens.put("refreshToken", refreshToken);
            tokens.put("accessToken", accessToken);

            dataDTO.setData(tokens);
            dataDTO.setSuccess(true);
            dataDTO.setMessage("로그인 성공");
        } catch (Exception e) {
            dataDTO.setSuccess(false);
            dataDTO.setMessage(e.getMessage());
        }
        System.out.println("로그인 컨트롤러 진행 완료");
        System.out.println(dataDTO);
        System.out.println("진행 완료 컨트롤러 아웃");
        return dataDTO;

    }

    // 디테일
    @PostMapping("detail")
    public DataDTO postDetail(@RequestBody MembersDTO mdto,
            @RequestHeader(value = "인증", required = false) String token) {
        DataDTO dataDTO = new DataDTO();
        try {
            log.info("아이디 : " + mdto.getMemEmail());
            if (token == null || !token.startsWith("Bearer ")) {
                throw new Exception("토큰이 유효하지 않습니다.");
            }
            // 토큰 검증
            String memEmail = jwtUtil.validateAndExtractmemEmail(token.substring(7));
            log.info(memEmail);
            log.info(mdto.getMemEmail());
            if (!memEmail.equals(mdto.getMemEmail())) {
                dataDTO.setSuccess(false);
                throw new IllegalArgumentException("기 로그인한 사용자 정보가 다릅니다.");
            }
            MembersDTO member = memberService.getMemberDetail(mdto.getMemEmail());

            if (member == null) {
                throw new IllegalArgumentException("가입정보가 없습니다.");
            }
            dataDTO.setSuccess(true);
            dataDTO.setData(member);
        } catch (Exception e) {
            dataDTO.setSuccess(false);
            dataDTO.setMessage(e.getMessage());
        }
        return dataDTO;
    }


    @PostMapping("logout")
    public DataDTO postLogout(@RequestHeader(value = "인증", required = false) String token,
            HttpServletResponse response) {
        DataDTO dataDTO = new DataDTO();
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                throw new Exception("토큰이 유효하지 않습니다.");
            }
            // 토큰 검증

            // 리프레시 토큰 삭제
            String memEmail = jwtUtil.validateAndExtractmemEmail(token.substring(7));
            memberService.deleteRefreshToken(memEmail);

            //액세스 토큰 쿠키 삭제
            Cookie cookie = new Cookie("access_Token", null);
            cookie.setMaxAge(0);
            cookie.setPath("/"); //모든 경로에서 삭제
            response.addCookie(cookie);

            dataDTO.setSuccess(true);
        } catch (Exception e) {
            dataDTO.setSuccess(false);
        }
        return dataDTO;
    }    

    // 회원가입 API
    @PostMapping("signup")
    public DataDTO signUp(@RequestBody MembersDTO membersDTO) {
        log.info("회원가입 컨트롤러 진입");
        System.out.println("회원가입 컨트롤러 진입");
        DataDTO response = new DataDTO();

        try {
            // 회원가입 로직
            // 비밀번호 암호화
            log.info("회원가입 컨트롤러 입력 비밀번호 암호화");
            System.out.println("암호화 전 : " + membersDTO.getMemSecret());
            System.out.println("회원가입 컨트롤러 입력 비밀번호 암호화");
            //String encodedPassword = passwordEncoder.encode(membersDTO.getMemSecret());
            // System.out.println(encodedPassword);
            // log.info(encodedPassword);
            // membersDTO.setMemSecret(encodedPassword);

            // 회원 정보 DB에 저장
            System.out.println("회원가입 컨트롤러 DB에 저장");
            memberService.insertUser(membersDTO);

            System.out.println("회원가입 컨트롤러 DB에 저장 결과 반환 진입");
            response.setSuccess(true);
            response.setMessage("회원가입 성공");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("회원가입 실패: " + e.getMessage());
        }

        System.out.println("회원가입 컨트롤러 진행 완료");
        return response;
    }


    

    
}
