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


    // 1. memEmail을 통해 memId를 찾는다
    Integer memId = memberMapper.findMemIdByEmail(memEmail); // memberMapper에 추가된 메서드 호출

    // 2. memId가 null이 아닌 경우에만 refresh token을 저장한다
    if (memId != null) {
        memberMapper.saveRefreshToken(memId, refresh_token, expiry_date);
    } else {
        // memId가 null인 경우 예외 처리
        throw new RuntimeException("Member with email " + memEmail + " not found.");
    }
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
