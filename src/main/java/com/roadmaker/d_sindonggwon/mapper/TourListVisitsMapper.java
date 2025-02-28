package com.roadmaker.d_sindonggwon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TourListVisitsMapper {
    // 조회수 증가
    @Update("UPDATE visits SET viewsid = viewsid + 1 WHERE destiid = #{destiid}")
    void updateViews(int destiid);

    // 특정여행지 조회수 가져오기
    @Select("SELECT viewsid FROM visits WHERE destiid = #{destiid}")
    int getViews(int destiid);

}
