package com.roadmaker.f_hwangjinsang.mapper.search;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.roadmaker.f_hwangjinsang.dto.search.SearchDTO;

@Mapper
public interface SearchMapper {

    // 검색어 저장
    @Insert("INSERT INTO searchword(searchword,memid,searchdate) VALUES(#{searchword},#{memid},NOW())")
    public Integer searchKeywordInsert(SearchDTO search);

}
