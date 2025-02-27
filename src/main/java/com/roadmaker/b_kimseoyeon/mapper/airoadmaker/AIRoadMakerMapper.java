package com.roadmaker.b_kimseoyeon.mapper.airoadmaker;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.roadmaker.b_kimseoyeon.dto.airoadmaker.DestinationDTO;
import com.roadmaker.b_kimseoyeon.dto.airoadmaker.KeywordDTO;

@Mapper
public interface AIRoadMakerMapper {

    //키워드
    @Select("SELECT keymid,keyctg1,keyctg2,keyword FROM keymanage WHERE keyctg1 LIKE '지역' ")
    public List<KeywordDTO> findCategory();

    //여행지 지역별로 가지고 오기 
    @Select("SELECT destiid, destiname, destiaddress, imgsname1 FROM destinations WHERE destiaddress LIKE CONCAT('%', #{region}, '%') AND destiid BETWEEN 1544 AND 2343 ORDER BY RAND()")
    public List<DestinationDTO> selectDestinationsByRegion(@Param("region") String region);

    //여행지 전체 가지고 오기 
    @Select("SELECT destiid, destiname, destiaddress, imgsname1 FROM destinations " +
    "WHERE destiid BETWEEN 1544 AND 2343 " +
    "ORDER BY RAND()")
    public List<DestinationDTO> selectAllDestinations();
}
