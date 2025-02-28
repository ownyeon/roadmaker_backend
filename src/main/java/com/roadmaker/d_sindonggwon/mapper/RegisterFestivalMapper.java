package com.roadmaker.d_sindonggwon.mapper;

import com.roadmaker.d_sindonggwon.dto.RegisterFestivalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface RegisterFestivalMapper {

    @Insert("INSERT INTO festival (festname, festcont, feststart, festend, festdesc, imgsname1, imgsname2) " +
            "VALUES (#{festname}, #{festcont}, #{feststart}, #{festend}, #{festdesc}, #{imgsname1}, #{imgsname2})")
    void registerFestival(RegisterFestivalDTO registerFestivalDTO);
}
