package com.roadmaker.d_sindonggwon.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.roadmaker.d_sindonggwon.dto.FestivalListDTO;

@Mapper
public interface FestivalListMapper {

    @Select("SELECT festid, festcrawlid, festname, festcont, feststart, " + 
            "festend, festdesc, imgsname1, imgfname1, imgsname2, imgfname2 " +
            "FROM festival")
    List<FestivalListDTO> getFestivalList();
}