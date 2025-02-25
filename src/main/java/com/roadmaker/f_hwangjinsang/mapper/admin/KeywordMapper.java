package com.roadmaker.f_hwangjinsang.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.roadmaker.f_hwangjinsang.dto.admin.AdminDTO;

@Mapper
public interface KeywordMapper {

    // country 갯수 검색
    @Select("SELECT COUNT(keyctg2) FROM keymanage WHERE keyctg2 = #{keyctg2}")
    public Integer countCountry(AdminDTO admin);

    // 키워드 관리 전체 검색
    @Select("SELECT keymid,keyctg1,keyctg2,keyword FROM keymanage")
    public List<AdminDTO> getCategory();

    // 키워드 관리 입력
    @Insert("INSERT INTO keymanage(keyctg1,keyctg2,keyword) VALUES(#{keyctg1},#{keyctg2},#{keyword})")
    public Integer insertKeyword(AdminDTO admin);

    // keyctg2 수정
    @Update("UPDATE keymanage set keyctg2 = #{afterValue} WHERE keyctg2 = #{keyctg2}")
    public Integer updateKeyctg2(AdminDTO admin);

    // keyword 수정
    @Update("UPDATE keymanage set keyword = #{afterValue} WHERE keymid = #{keymid}")
    public Integer updateKeyword(AdminDTO admin);

    // keyword 삭제
    @Delete("DELETE FROM keymanage WHERE keymid = #{keymid}")
    public Integer deleteKeyword(AdminDTO admin);

    // Country 삭제
    @Delete("DELETE FROM keymanage WHERE keyctg2 = #{keyctg2}")
    public Integer deleteCountry(AdminDTO admin);

    // 키워드별 여행지 목록 불러오기
    @Select("SELECT d.destiname destiname, d.destiid destiid, k.keyuid keyuid " +
            " FROM keyused k JOIN destinations d " +
            " ON k.destiid = d.destiid " +
            " WHERE k.keymid = #{keymid} ")
    public List<AdminDTO> getKeywordByDestinationsList(AdminDTO admin);

    // 키워드 관리 여행지 추가 여행지 리스트 부르기
    @Select("SELECT destiname,destiid FROM destinations")
    public List<AdminDTO> getDestinationList();

    // 키워드 관리 여행지 추가 여행지 리스트 부르기
    @Select("SELECT destiname,destiid FROM destinations WHERE destiname LIKE CONCAT('%', #{searchWord}, '%')")
    public List<AdminDTO> getDestinationListBySearchWord(AdminDTO admin);

    // 키워드 관리 키워드 내 여행지 추가
    @Insert("INSERT INTO keyused(destiid,Keymid) VALUES(#{destiid},#{keymid})")
    public Integer insertKeyused(AdminDTO dto);

    // 키워드 관리 키워드 내 여행지 삭제
    @Delete("DELETE FROM keyused WHERE keyuid = #{keyuid}")
    public Integer deleteKeyused(AdminDTO admin);
}
