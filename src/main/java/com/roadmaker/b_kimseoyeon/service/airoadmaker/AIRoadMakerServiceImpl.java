package com.roadmaker.b_kimseoyeon.service.airoadmaker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.b_kimseoyeon.dto.airoadmaker.DestinationDTO;
import com.roadmaker.b_kimseoyeon.dto.airoadmaker.KeywordDTO;
import com.roadmaker.b_kimseoyeon.mapper.airoadmaker.AIRoadMakerMapper;

@Service
public class AIRoadMakerServiceImpl implements AIRoadMakerService {

    @Autowired
    private AIRoadMakerMapper mapper;

    @Override // AI 카테고리 가져오기(키워드)
    public List<KeywordDTO> getCateCountry() {
        return mapper.findCategory();
    }

    @Override
    public List<DestinationDTO> getDestinationsByRegion(String region) {
        if(region == null){
            return mapper.selectAllDestinations();
        } else {
            return mapper.selectDestinationsByRegion(region);
        }
    }
    

}
