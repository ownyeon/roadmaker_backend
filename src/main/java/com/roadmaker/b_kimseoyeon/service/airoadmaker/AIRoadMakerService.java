package com.roadmaker.b_kimseoyeon.service.airoadmaker;

import java.util.List;

import com.roadmaker.b_kimseoyeon.dto.airoadmaker.DestinationDTO;
import com.roadmaker.b_kimseoyeon.dto.airoadmaker.KeywordDTO;

public interface AIRoadMakerService {
    // AI 카테고리 전부 가져오기 (키워드)
    public List<KeywordDTO> getCateCountry();

    // AI 여행지 가지고 오기 
    public List<DestinationDTO> getDestinationsByRegion(String region);

};