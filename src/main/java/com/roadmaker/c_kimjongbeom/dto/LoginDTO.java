package com.roadmaker.c_kimjongbeom.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 새로운 로그인 응답 DTO 정의
public class LoginDTO {
    private String accessToken;
    private String refreshToken;
    private Integer memId;
    private String memEmail;
    private String memRole;
    private String memNickname;
    private Integer memStatus;
    private TokensDTO tokens; //토큰 정보 관리
    private MembersDTO data;
    private boolean success;
    private String message;
}
