package com.roadmaker.b_kimseoyeon.dto.airoadmaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordDTO {

    //키워드 관련 
    private Integer keymid;
    private String keyctg1, keyctg2, keyword, keydesc, imgsname1, imgfname1, imgsname2, imgfname2;
    

}
