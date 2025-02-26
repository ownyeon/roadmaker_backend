package com.roadmaker.e_choesuji.service;

import java.util.List;


import com.roadmaker.e_choesuji.dto.FindTourDTO;

public interface FindTourService {
    public List<FindTourDTO> getFindTourList();            //여행지 목록조회
    public FindTourDTO getTourListDetail(Long destiid);   //여행지 상세조회
    public List<FindTourDTO> getTourListByKeywordAndRegion(String keyctg2, String themeId);  //테마별 
};