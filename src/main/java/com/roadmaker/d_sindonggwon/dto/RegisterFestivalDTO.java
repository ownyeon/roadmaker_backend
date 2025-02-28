package com.roadmaker.d_sindonggwon.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterFestivalDTO {
    private String festname;
    private String festcont;
    private Date feststart;
    private Date festend;
    private String festdesc;
    private String imgsname1;
    private String imgsname2;
    
}
