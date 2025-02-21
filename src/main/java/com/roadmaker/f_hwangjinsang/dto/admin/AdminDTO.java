package com.roadmaker.f_hwangjinsang.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private Integer keymid;
    private String keyctg1, keyctg2, keyword;
}
