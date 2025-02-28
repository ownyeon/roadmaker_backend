package com.roadmaker.e_choesuji.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.roadmaker.e_choesuji.dto.FindTourDTO;
import com.roadmaker.e_choesuji.dto.HotelRegistrationRequestDTO;

public interface FindTourService {
    public List<FindTourDTO> getFindTourList(); // 여행지 목록조회

    public FindTourDTO getTourListDetail(Long destiid); // 여행지 상세조회

    void registerHotel(HotelRegistrationRequestDTO request) throws Exception;   //신규관광지등록록

}
