package com.roadmaker.f_hwangjinsang.mapper.airoadmaker;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.roadmaker.f_hwangjinsang.dto.airoadmaker.AIRoadMakerDTO;

@Mapper
public interface AIRoadMakerMapper {

    @Select("SELECT keymid,keyctg1,keyctg2,keyword FROM keymanage WHERE keyctg1 LIKE '지역' ")
    public List<AIRoadMakerDTO> findCategory();
}
