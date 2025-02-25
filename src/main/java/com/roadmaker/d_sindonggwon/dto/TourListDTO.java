package com.roadmaker.d_sindonggwon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourListDTO {
    private int destiid;
    private String destiname;
    private String destidesc;
    private String destiaddress;
    private String destiopenhr;
    private String destiparkavail;
    private String desticontact;
    private double destilongit;
    private double destilatit;
    private String destiregdate;
    private String destiappear;
    private String imgsname1;
    private String imgfname1;
    private String imgsname2;
    private String imgfname2;
    private String imgsname3;
    private String imgfname3;
    private String imgsname4;
    private String imgfname4;
    private String keyword;
    private String keyctg1;
    private String keyctg2;
}
