package com.roadmaker.c_kimjongbeom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.roadmaker.c_kimjongbeom.dto.DataDTO2;
import com.roadmaker.c_kimjongbeom.dto.MembersDTO;
import com.roadmaker.c_kimjongbeom.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    // MemberMapper를 주입받아 회원 정보를 조회
    private final MemberMapper memberMapper;

    // 사용자의 이메일로 회원 정보를 조회하여 UserDetails 객체를 생성
    @Override
    public UserDetails loadUserByUsername(String memEmail) throws UsernameNotFoundException {
        log.info("loadUserByUsername 시작: 이메일 = {}", memEmail);  // 로그 추가: 메서드 시작 시 입력된 이메일 출력
        
        // 이메일로 회원 정보를 조회
        MembersDTO member = memberMapper.findByEmail(memEmail);
        
        if (member == null) {
            log.warn("사용자 정보 없음: {}", memEmail);  // 로그 추가: 사용자 정보가 없을 때 경고 로그
            throw new UsernameNotFoundException("이메일을 확인해주세요.");  // 예외 발생
        }

        log.info("회원 정보 조회 완료: {}", member);  // 로그 추가: 회원 정보가 정상적으로 조회되었을 때 로그 출력

        // MembersDTO를 기반으로 DataDTO2 생성
        DataDTO2 dataDTO2 = new DataDTO2(member.getMemId(), member.getMemEmail(), member.getMemRole(), member.getMemNickname(), member.getMemStatus());
        
        log.info("DataDTO2 객체 생성 완료: {}", dataDTO2);  // 로그 추가: DataDTO2 생성 완료 로그 출력

        // 사용자 권한을 리스트로 생성 (회원의 역할을 권한으로 추가)
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getMemRole()));  // 역할을 ROLE_ 접두어와 함께 추가

        log.info("사용자 권한 설정 완료: {}", authorities);  // 로그 추가: 권한 리스트 설정 완료 로그 출력

        // UserDetails 객체를 생성하여 반환 (Spring Security의 User 객체 사용)
        UserDetails userDetails = new User(member.getMemEmail(), member.getMemSecret(), authorities);

        log.info("UserDetails 객체 반환: {}", userDetails);  // 로그 추가: 반환할 UserDetails 객체 로그 출력

        return userDetails;
    }
}
