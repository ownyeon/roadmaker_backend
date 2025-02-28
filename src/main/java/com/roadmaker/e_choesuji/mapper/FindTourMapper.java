package com.roadmaker.e_choesuji.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.roadmaker.e_choesuji.dto.FindTourDTO;
import com.roadmaker.e_choesuji.dto.HotelRegistrationRequestDTO;

@Mapper
public interface FindTourMapper {

        // 여행지 목록 전체조회
        // destinations 테이블에서 destiname만 불러오는 쿼리
        @Select("<script>" +
                        "SELECT d.destiid, d.destiname, d.destidesc, d.destiaddress, d.destiopenhr, " +
                        "       d.destiparkavail, d.desticontact, d.destilongit, d.destilatit, " +
                        "       d.destiregdate, d.destiappear, d.imgsname1, d.imgfname1, d.imgsname2, " +
                        "       d.imgfname2, d.imgsname3, d.imgfname3, d.imgsname4, d.imgfname4, " +
                        "       GROUP_CONCAT(DISTINCT m.keyword) AS keyword, " +
                        "       GROUP_CONCAT(DISTINCT m.keyctg1) AS keyctg1, " +
                        "       GROUP_CONCAT(DISTINCT m.keyctg2) AS keyctg2 " +
                        "FROM destinations d " +
                        "JOIN keyused u ON d.destiid = u.destiid " +
                        "LEFT JOIN keymanage m ON u.keymid = m.keymid " +
                        "GROUP BY d.destiid" +
                        "</script>")
        List<FindTourDTO> getFindTourList(); // 결과는 DataDTO 객체의 리스트로 반환됩니다.

        // 여행지목록상세조회
        @Select("<script>" +
                        "SELECT d.destiid, d.destiname, d.destidesc, d.destiaddress, d.destiopenhr, " +
                        "       d.destiparkavail, d.desticontact, d.destilongit, d.destilatit, " +
                        "       d.destiregdate, d.destiappear, d.imgsname1, d.imgfname1, d.imgsname2, " +
                        "       d.imgfname2, d.imgsname3, d.imgfname3, d.imgsname4, d.imgfname4, " +
                        "       GROUP_CONCAT(DISTINCT m.keyword) AS keyword, " +
                        "       GROUP_CONCAT(DISTINCT m.keyctg1) AS keyctg1, " +
                        "       GROUP_CONCAT(DISTINCT m.keyctg2) AS keyctg2 " +
                        "FROM destinations d " +
                        "LEFT JOIN keyused u ON d.destiid = u.destiid " +
                        "LEFT JOIN keymanage m ON u.keymid = m.keymid " +
                        "WHERE d.destiid = #{destiid} " +
                        "GROUP BY d.destiid" +
                        "</script>")
        FindTourDTO getTourListDetail(Long destiid);

        // 여행지 정보를 destinations 테이블에 삽입
        @Insert("INSERT INTO destinations (" +
                        "destiid, " +
                        "destiname, " +
                        "destiaddress, " +
                        "destidesc, " +
                        "destiopenhr, " +
                        "desticontact, " +
                        "destiholid, " +
                        "imgsname1, " +
                        "imgsname2, " +
                        "imgsname3, " +
                        "imgsname4) " +
                        "VALUES (" +
                        "1, " + // nextval을 삽입하여 직접 증가된 값을 사용
                        "#{destiname}, " +
                        "#{destiaddress}, " +
                        "#{destidesc}, " +
                        "#{destiopenhr}, " +
                        "#{desticontact}, " +
                        "#{destiholid}, " +
                        "#{imgsname1}, " +
                        "#{imgsname2}, " +
                        "#{imgsname3}, " +
                        "#{imgsname4})")
        void insertDestination(HotelRegistrationRequestDTO request);

        // 지역 목록 조회 쿼리
        @Select("SELECT DISTINCT keyctg2 FROM destinations")
        List<String> getRegions(); // 'destinations' 테이블에서 지역 목록 조회

        // 지역과 키워드를 기반으로 여행지 목록을 조회하는 쿼리
        @Select("SELECT * FROM destinations WHERE keyctg2 = #{keyctg2} AND (destiname LIKE CONCAT('%', #{keyword}, '%') OR destidesc LIKE CONCAT('%', #{keyword}, '%'))")
        List<FindTourDTO> getTourListByKeywordAndRegion(@Param("keyctg2") String keyctg2,
                        @Param("keyword") String keyword);

}
