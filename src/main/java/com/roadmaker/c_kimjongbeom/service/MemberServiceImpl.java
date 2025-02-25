package com.roadmaker.c_kimjongbeom.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.c_kimjongbeom.dto.MembersDTO;
import com.roadmaker.c_kimjongbeom.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    // MemberMapper를 자동 주입
    @Autowired
    private MemberMapper memberMapper;

    // 회원 리스트를 반환하는 메서드
    @Override
    public List<MembersDTO> getMemberList() {
        log.info("getMemberList 시작");  // 로그 추가: 메서드 시작 로그
        List<MembersDTO> membersList = memberMapper.getMemberList();
        log.info("회원 목록 조회 완료, 총 {} 명", membersList.size());  // 로그 추가: 조회된 회원 목록 크기 출력
        return membersList;
    }

    // 특정 이메일에 해당하는 회원의 상세 정보를 반환하는 메서드
    @Override
    public MembersDTO getMemberDetail(String memEmail) {
        log.info("getMemberDetail 시작: 이메일 = {}", memEmail);  // 로그 추가: 이메일 정보 출력
        MembersDTO memberDetail = memberMapper.findByEmail(memEmail);
        
        if (memberDetail == null) {
            log.warn("이메일로 조회된 회원 정보가 없음: {}", memEmail);  // 로그 추가: 회원 정보가 없을 때 경고 로그
        } else {
            log.info("회원 정보 조회 완료: {}", memberDetail);  // 로그 추가: 회원 정보 조회 성공 시 로그 출력
        }

        return memberDetail;
    }

    // Refresh Token을 저장하는 메서드
    @Override
    public void saveRefreshToken(String memEmail, Integer memId, String refresh_token, Date expiry_date) {
        log.info("saveRefreshToken 시작: 이메일 = {}", memEmail);  // 로그 추가: 이메일 정보 출력
        
        // 1. 주어진 이메일로 회원 정보를 조회하여 memId를 확인
        MembersDTO mdto = memberMapper.findMemIdByEmail(memEmail);
        if (mdto == null) {
            log.error("회원 정보를 찾을 수 없음: {}", memEmail);  // 로그 추가: 회원 정보 조회 실패 로그
            throw new RuntimeException("Member with email " + memEmail + " not found.");
        }
        
        // 2. memId가 유효한 경우에만 refresh token을 저장
        if (memId != null) {
            log.info("memId {} 유효, refresh token 저장 시작", memId);  // 로그 추가: memId 유효 여부 로그
            memberMapper.saveRefreshToken(mdto.getMemId(), refresh_token, expiry_date);
            log.info("refresh token 저장 완료: {}", refresh_token);  // 로그 추가: refresh token 저장 완료 로그
        } else {
            log.error("유효하지 않은 memId: {}", memId);  // 로그 추가: memId가 유효하지 않음 로그
            throw new RuntimeException("Invalid memId: " + memId);
        }
    }

    // 주어진 이메일로 회원의 Refresh Token을 조회하는 메서드
    @Override
    public String findRefreshToken(String memEmail) {
        log.info("findRefreshToken 시작: 이메일 = {}", memEmail);  // 로그 추가: 이메일 정보 출력
        String refreshToken = memberMapper.findRefreshToken(memEmail);
        
        if (refreshToken == null) {
            log.warn("이메일에 해당하는 refresh token 없음: {}", memEmail);  // 로그 추가: refresh token 미존재 경고
        } else {
            log.info("refresh token 조회 완료: {}", refreshToken);  // 로그 추가: refresh token 조회 완료 로그
        }

        return refreshToken;
    }

    // 주어진 이메일로 회원의 Refresh Token을 삭제하는 메서드
    @Override
    public void deleteRefreshToken(String memEmail) {
        log.info("deleteRefreshToken 시작: 이메일 = {}", memEmail);  // 로그 추가: 이메일 정보 출력
        memberMapper.deleteRefreshToken(memEmail);
        log.info("refresh token 삭제 완료: 이메일 = {}", memEmail);  // 로그 추가: refresh token 삭제 완료 로그
    }

    // 새 회원을 DB에 추가하는 메서드
    @Override
    public void insertUser(MembersDTO membersDTO) {
        log.info("insertUser 시작: 회원 이메일 = {}", membersDTO.getMemEmail());  // 로그 추가: 회원 이메일 출력

        // 비밀번호를 암호화하는 부분 (이 부분은 실제로는 암호화 과정이 필요)
        String encodedPassword = membersDTO.getMemSecret();  // 현재는 단순히 비밀번호 그대로 사용
        membersDTO.setMemSecret(encodedPassword);

        log.info("비밀번호 암호화 완료: {}", encodedPassword);  // 로그 추가: 비밀번호 암호화 완료 로그

        // DB에 회원 정보를 저장
        memberMapper.insertMember(membersDTO);
        log.info("회원 DB 저장 완료: {}", membersDTO);  // 로그 추가: 회원 정보 DB 저장 완료 로그
    }
}
