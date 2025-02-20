package com.roadmaker.a_common.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.roadmaker.a_common.security.JwtUtil;
// import com.ict.edu3.service.MemberService;
// import com.ict.edu3.service.MyUserDetailService;
// import com.ict.edu3.vo.MembersVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // private final MemberService memberService;
    private final JwtUtil jwtUtil;
    // private final MyUserDetailService userDetailService;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil) { //, MyUserDetailService userDetailService,MemberService memberService
        this.jwtUtil = jwtUtil;
        // this.userDetailService = userDetailService;
        // this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2AuthenticationSuccessHandler:");
            // 현재 인증 객체가 OAuth2 기반으로 인증 되어있는지 확인 하는 코드
            // OAuth2 (IETF에서 개발된 공개 표준 인증 프로토콜)
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
                String provider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(); // 로그인제공자

                // CustomerOAuth2UserService 저장한 정보 호출
                // OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
                // String id = oAuth2User.getAttribute("id");
                // String name = oAuth2User.getAttribute("name");
                // String email = oAuth2User.getAttribute("email");
                // String token = jwtUtil.generateAccessToken(id);

                // DB 저장
                // id로 DB에 있는지 검사
                // MembersVO mvo = memberService.getMemberDetail(id);

                // if (mvo == null) {
                //     // 없으면 생성하고 token 넘기기
                //     MembersVO mvo2 = new MembersVO();
                //     mvo2.setM_id(id);
                //     mvo2.setM_name(name);
                //     mvo2.setSns_provider(provider);
                //     if (provider.equals("kakao")) {
                //         mvo2.setSns_email_kakao(email);
                //     } else if (provider.equals("naver")) {
                //         mvo2.setSns_email_naver(email);
                //     } else if (provider.equals("google")) {
                //         mvo2.setSns_email_google(email);
                //     }
                //     memberService.insertUser(mvo2);
                // }
                

                // Cookie cookie = new Cookie("authToken", token);
                // // cookie.setHttpOnly(true); // js에서 접근 불가
                // cookie.setSecure(false); // https에서만 가능 나중에 true
                // cookie.setPath("/"); // 전체 도메인에서 사용가능
                // response.addCookie(cookie);
                // response.sendRedirect("http://3.38.253.110/");

                // // 리다이렉트
                // String redirectUrl = String.format(
                // "http://localhost:3000/login?provider=%s&token=%s&username=%s&name=%s&email=%s",
                // URLEncoder.encode(provider, StandardCharsets.UTF_8),
                // URLEncoder.encode(token, StandardCharsets.UTF_8),
                // URLEncoder.encode(id, StandardCharsets.UTF_8),
                // URLEncoder.encode(name, StandardCharsets.UTF_8),
                // URLEncoder.encode(email, StandardCharsets.UTF_8));
                // //단점 : 보안
                // String redirectUrl = String.format("http://localhost:3000/login?token=%s",
                // URLEncoder.encode(token, StandardCharsets.UTF_8));
                
            }
        } catch (Exception e) {
            log.info("error" + e.toString());
            response.sendRedirect("/login?error");
        }
    }
}
