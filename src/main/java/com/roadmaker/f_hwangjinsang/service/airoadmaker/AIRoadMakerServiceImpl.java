package com.roadmaker.f_hwangjinsang.service.airoadmaker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.f_hwangjinsang.dto.airoadmaker.AIRoadMakerDTO;
import com.roadmaker.f_hwangjinsang.mapper.airoadmaker.AIRoadMakerMapper;

@Service
public class AIRoadMakerServiceImpl implements AIRoadMakerService {

    @Autowired
    private AIRoadMakerMapper mapper;

    @Override // AI 카테고리 가져오기
    public List<AIRoadMakerDTO> getCateCountry() {
        return mapper.findCategory();
    }
}
