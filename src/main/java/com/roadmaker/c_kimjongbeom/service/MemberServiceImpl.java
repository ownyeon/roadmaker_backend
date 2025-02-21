package com.roadmaker.c_kimjongbeom.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.roadmaker.c_kimjongbeom.dto.MembersDTO;
import com.roadmaker.c_kimjongbeom.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    // 맴버리스트
    public List<MembersDTO> getMemberList() {
        return memberMapper.getMemberList();
    }

    @Override
    public MembersDTO getMemberDetail(String memEmail) {
        System.out.println("서비스임플 getmemberdetail 진입");
        return memberMapper.findByEmail(memEmail);
    }

    @Override
    public void saveRefreshToken(String memEmail, String refresh_token, Date expiry_date) {
        System.out.println("서비스임플 saverefreshtoken 진입");

        memberMapper.saveRefreshToken(memEmail,refresh_token,expiry_date);
    }

    @Override
    public String findRefreshToken(String memEmail) {
        return memberMapper.findRefreshToken(memEmail);
    }

    @Override
    public void deleteRefreshToken(String memEmail) {
        memberMapper.deleteRefreshToken(memEmail);
    }

    @Override
    public void insertUser(MembersDTO membersDTO) {
        // 비밀번호 암호화
        String encodedPassword = membersDTO.getMemSecret();
        membersDTO.setMemSecret(encodedPassword);

        // DB에 회원 정보 저장
        memberMapper.insertMember(membersDTO);
    }
    
}
