package com.roadmaker.b_kimseoyeon.dto.airoadmaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

//여행지 관련
public class DestinationDTO {
    private Integer destiid;
    private String destiname, destidesc, destiaddress, destiopenhr, destiparkavail, desticontact,
                    destiregdate, destiappear, imgsname1, imgfname1, imgsname2, imgfname2,  imgsname3, imgfname3, imgsname4, imgfname4;
    private double destilongit, destilatit;

}
