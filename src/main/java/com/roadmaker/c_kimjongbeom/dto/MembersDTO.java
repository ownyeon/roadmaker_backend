package com.roadmaker.c_kimjongbeom.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // 빌더 클래스 사용 가능
public class MembersDTO {

    // members 테이블의 정보를 표현
    private Integer memId;            // 회원 인덱스.

    private String memEmail;       // 아이디(이메일)

    private String memSecret;      // 비밀번호(토큰)
    private String memSecret2;      // 여분 컬럼
    private String memNickname;    // 닉네임
    private String memGender;      // 성별
    private Integer memAge;        // 나이
    private String memSns;         // SNS 유형
    private String memRole;        // 역할(일반회원/관리자)
    private Integer memStatus;     // 활동상태
    private String memRecentIp;    // 최근접속IP
    private String memRecentLogin; // 최근 로그인
    private String imgSname1;      // 출력 파일명(프로필 이미지)
    private String imgFname1;      // 저장 파일명(프로필 이미지)
    private LocalDateTime memJoinDate;  // 가입일자
    private LocalDateTime memWithdrawDate; // 탈퇴일자
}


/*
※ 기능
   ; 데이터베이스의 members 테이블과 연결.
     회원 정보를 담는 모델로, 회원의 기본적인 정보와 
   가입 및 탈퇴 시간, 역할 등을 관리
   회원 정보를 데이터베이스에 저장하고 관리하는 역할로,
   데이터베이스의 members 테이블에 해당하는 데이터를 표현함.

Lombok
@Data: getter, setter, toString, equals, hashCode 메서드를 자동으로 생성.
@Builder: 객체를 생성할 때 빌더 패턴을 사용할 수 있게 해줌.
@AllArgsConstructor: 모든 필드를 매개변수로 받는 생성자를 자동으로 생성
@NoArgsConstructor: 기본 생성자를 자동으로 생성
 */