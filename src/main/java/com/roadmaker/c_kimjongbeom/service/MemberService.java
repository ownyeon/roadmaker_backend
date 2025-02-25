package com.roadmaker.c_kimjongbeom.service;

import java.util.Date;
import java.util.List;

import com.roadmaker.c_kimjongbeom.dto.MembersDTO;

public interface MemberService {

    // 로그인 시 필요한 함수 목록

    // 모든 회원 정보 목록을 반환하는 메서드
    public List<MembersDTO> getMemberList();

    // 특정 이메일을 기준으로 회원 상세 정보를 반환하는 메서드
    public MembersDTO getMemberDetail(String memEmail);

    // Refresh Token을 저장하는 메서드
    // memEmail: 회원 이메일, memId: 회원 ID, refresh_token: 새로 생성된 refresh token, expiry_date: refresh token의 만료일
    void saveRefreshToken(String memEmail, Integer memId, String refresh_token, Date expiry_date);

    // 주어진 이메일에 해당하는 회원의 Refresh Token을 반환하는 메서드
    String findRefreshToken(String memEmail);

    // 주어진 이메일에 해당하는 회원의 Refresh Token을 삭제하는 메서드
    void deleteRefreshToken(String memEmail);

    // 새 회원을 DB에 추가하는 메서드
    public void insertUser(MembersDTO membersDTO); // 회원 정보가 담긴 DTO를 받아 회원을 추가
}
