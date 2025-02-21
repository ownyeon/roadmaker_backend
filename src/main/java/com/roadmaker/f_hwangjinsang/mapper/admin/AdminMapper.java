package com.roadmaker.f_hwangjinsang.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.roadmaker.f_hwangjinsang.dto.admin.AdminDTO;

@Mapper
public interface AdminMapper {

    @Select("SELECT keymid,keyctg1,keyctg2,keyword FROM keymanage")
    public List<AdminDTO> getCategory();
}
