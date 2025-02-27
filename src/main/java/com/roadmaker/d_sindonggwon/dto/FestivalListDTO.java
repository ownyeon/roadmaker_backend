package com.roadmaker.d_sindonggwon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FestivalListDTO {
    private int festid;
    private int festcrawlid;
    private String festname;
    private String festcont;
    private String feststart;
    private String festend;
    private String festdesc;
    private String imgsname1;
    private String imgfname1;
    private String imgsname2;
    private String imgfname2;
    
}