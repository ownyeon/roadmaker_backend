package com.roadmaker.d_sindonggwon.service;

import java.util.List;

import com.roadmaker.d_sindonggwon.dto.TourListDTO;
import com.roadmaker.d_sindonggwon.dto.TourListDetailDTO;

public interface TourListService {
    // 여행지 리스트 불러오기
    public List<TourListDTO> getTourList();
    
    // 여행지 상세정보 불러오기
    public TourListDetailDTO getTourListDetail(Long destiid);
}