package com.roadmaker.c_kimjongbeom.service;

import java.util.Date;
import java.util.List;

import com.roadmaker.c_kimjongbeom.dto.MembersDTO;

public interface MemberService {

    //로그인시 필요한 함수 목록
    public List<MembersDTO> getMemberList();
    public MembersDTO getMemberDetail(String memEmail);

    void saveRefreshToken(String memId, String refresh_token, Date expiry_date);
    String findRefreshToken(String memEmail);
    void deleteRefreshToken(String memEmail);
   
    public void insertUser(MembersDTO membersDTO);
    



    
}
