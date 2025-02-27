package com.roadmaker.b_kimseoyeon.controller.airoadmaker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.a_common.dto.DataDTO;
import com.roadmaker.b_kimseoyeon.dto.airoadmaker.DestinationDTO;
import com.roadmaker.b_kimseoyeon.service.airoadmaker.AIRoadMakerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/airoadmaker")
@RequiredArgsConstructor
public class AIRoadMakerController {

    @Autowired
    private AIRoadMakerService service;

    // AI로드메이커 지역 카테고리 불러오기
    @PostMapping("/cate")
    public DataDTO searchCate() {
        DataDTO data = new DataDTO();
        try {
            data.setData(service.getCateCountry());
        } catch (Exception e) {
           data.setSuccess(false);
           data.setMessage("카테고리 불러오기 실패");
        }
        return data;
    }
    
    // AI로드메이커 여행지 불러오기 
    @GetMapping("/destinations")
    public List<DestinationDTO> destinations(@RequestParam(required = false) String region) {
        List<DestinationDTO> destinations =  service.getDestinationsByRegion(region);
    


        return destinations;

    }
    
}
