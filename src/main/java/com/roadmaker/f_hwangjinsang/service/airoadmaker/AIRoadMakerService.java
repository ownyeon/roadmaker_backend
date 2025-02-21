package com.roadmaker.f_hwangjinsang.service.airoadmaker;

import java.util.List;

import com.roadmaker.f_hwangjinsang.dto.airoadmaker.AIRoadMakerDTO;

public interface AIRoadMakerService {
    // AI 카테고리 전부 가져오기
    public List<AIRoadMakerDTO> getCateCountry();
};