package com.roadmaker.a_common.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.roadmaker.a_common.security.JwtRequestFilter;
import com.roadmaker.a_common.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SecurityConfig {

    // private final MemberService memberService;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtUtil jwtUtil;
    // private final MyUserDetailService userDetailService;

    // 생성자
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, JwtUtil jwtUtil) { //// , MyUserDetailService
                                                                                //// userDetailService,MemberService
                                                                                //// memberService
        log.info("SecurityConfig 호출 : ");
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUtil = jwtUtil;
        // this.userDetailService = userDetailService;
        // this.memberService = memberService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain 호출 \n");
        http
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(configurationSource()))
                // CSRF 보호 비활성화 (JWT 사용시 일반적으로 사용)
                .csrf(csrf -> csrf.disable())
                // 요청별 권한 설정
                // 개발중에만 추후 바꿔야함
                //.authorizeRequests(authorize -> authorize
                //        .anyRequest().permitAll()) // 모든 요청을 인증 없이 허용
                 .authorizeRequests(authorize -> authorize
                // .requestMatchers("/upload/**").permitAll() // URL 경로
                // .requestMatchers("/oauth2/**").permitAll() // URL 경로
                 .requestMatchers("/api/oauth2/authorization/**").permitAll() // URL 경로
                // // 특정 URL에 인증없이 허용
                // .requestMatchers("/api/members/register", "/api/members/login").permitAll()
                // // 나머지는 인증 필요
                // .anyRequest().authenticated()
                )
                // oath2Login 설정
                // successHandler => 로그인 성공 시 호출
                // userInfoEndpoint => 인증과정에서 인증된 사용자에 대한 정보를 제공 하는 API 엔드포인트
                // (사용자 정보를 가져오는 역할을 한다.) OAuth2 빌드 필수~
                // .oauth2Login(oauth2 -> oauth2
                // .successHandler(oAuth2AuthenticationSuccessHandler())
                // .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService())))

                // 사용자 요청이 오면 먼저 jwtRequestFilter가 실행되어 JWT 토큰을 검증한 후
                // 이상이 없으면 SpringSecurity의 인증된 사용자로 처리됩니다.
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // @Bean
    // // 동의 항목 처리
    // OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
    // return new OAuth2AuthenticationSuccessHandler(jwtUtil, userDetailService,
    // memberService);
    // }

    @Bean
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new CustomerOAuth2UserService();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 허용할 Origin 설정
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        // 허용할 http 메서드 설정
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 허용할 헤더 설정
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        // 인증정보 허용(쿠키나 인증 헤더를 사용할 경우 true)
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
