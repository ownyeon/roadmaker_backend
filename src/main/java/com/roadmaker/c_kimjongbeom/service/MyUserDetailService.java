package com.roadmaker.c_kimjongbeom.service;

import java.util.ArrayList;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.roadmaker.c_kimjongbeom.dto.MembersDTO;
import com.roadmaker.c_kimjongbeom.mapper.MemberMapper;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService{
    
    private final MemberMapper memberMapper;

    // id를 받아서 members 테이블에 해당 id가 있는지 찾기
    @Override
    public UserDetails loadUserByUsername(String memEmail) throws UsernameNotFoundException {
        System.out.println("MyUserDetailService 아이디 찾기 진입");
        log.info("MyUserDetailService 아이디 찾기 진입");
        MembersDTO member = memberMapper.findByEmail(memEmail);
        System.out.println("MyUserDetailService 아이디 찾기 과정 완료");
        log.info("MyUserDetailService 아이디 찾기 과정 완료");

        if(member==null){
            System.out.println("MyUserDetailService 사용자 정보 없을시");
            log.warn("MyUserDetailService 사용자 정보 없음: " + memEmail);
            throw new UsernameNotFoundException("이메일을 확인해주세요 : "+memEmail);
        }
        System.out.println("MyUserDetailService 사용자 정보 전송");
        log.info("MyUserDetailService 사용자 정보 전송");


        return new User(member.getMemEmail(), member.getMemSecret(), new ArrayList<>());
        }
    // 정보를 받아서 회원가입하기
    public void registerUser(MembersDTO mDto){
        memberMapper.insertMember(mDto);
    }
}
