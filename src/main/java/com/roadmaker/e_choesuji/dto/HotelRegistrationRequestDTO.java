package com.roadmaker.e_choesuji.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelRegistrationRequestDTO {

    private long destiid;
    private String destiname;
    private String destiaddress;
    private String destidesc;
    private String destiopenhr;
    private String website;
    private String keywords;
    private String desticontact;
    private String destiholid;
    private String imgsname1;
    private String imgsname2;
    private String imgsname3;
    private String imgsname4;
}
