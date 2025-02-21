package com.roadmaker.c_kimjongbeom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {
    private String memEmail;
    private String memSecret;
    private String memNickname;

}
