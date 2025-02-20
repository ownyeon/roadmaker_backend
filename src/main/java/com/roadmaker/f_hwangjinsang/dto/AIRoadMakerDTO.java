package com.roadmaker.f_hwangjinsang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIRoadMakerDTO {

    private Integer keymid;
    private String keyctg1, keyctg2, keyword, keydesc, imgsname1, imgfname1, imgsname2, imgfname2;
}
