package com.roadmaker.d_sindonggwon.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.roadmaker.d_sindonggwon.dto.TourListDTO;
import com.roadmaker.d_sindonggwon.dto.TourListDetailDTO;

@Mapper
public interface TourListMapper {

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
        "GROUP BY d.destiid" +
        "</script>")
    List<TourListDTO> getTourList();

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
TourListDetailDTO getTourListDetail(Long destiid);
}
