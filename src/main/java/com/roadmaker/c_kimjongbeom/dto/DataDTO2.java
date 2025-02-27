package com.roadmaker.c_kimjongbeom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 토큰의 사용자 정보
public class DataDTO2 {
    private Integer memId;
    private String memEmail;
    private String memRole;
    private String memNickname;
    private Integer memStatus;
}
