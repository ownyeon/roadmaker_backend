package com.roadmaker.c_kimjongbeom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokensDTO {
    private String accessToken;
    private String refreshToken;
}


/*
    클라이언트로부터 받은 회원 가입 데이터를 담음.

    회원 가입시 사용자가 입력할 값들을 기준으로 
    member 테이블에서 호출
    코드에 의해 들어갈 값 등은 포함하지 않음. 

    member테이블의 컬럼 중 사용자가 직접 입력하는 데이터:

    - email (이메일)
    - password (비밀번호)
    - nickname (닉네임)
    - gender (성별)
    - name (이름)
    - age (나이)

자동으로 처리되는 데이터:
    - id(회원 인덱스)
    - memJoinDate (가입일시): 서버에서 자동으로 설정
    - memWithdrawDate (탈퇴일시): 회원이 탈퇴한 경우 자동으로 설정
    - memRole: 회원가입 시 기본값을 설정 (예: "user" 또는 "member")

입력시 자동으로 처리되는 데이터를 합산 시키기 위해 Builder를 활성화 시킴.

 */
