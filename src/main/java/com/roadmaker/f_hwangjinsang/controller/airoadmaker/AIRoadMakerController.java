package com.roadmaker.f_hwangjinsang.controller.airoadmaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.f_hwangjinsang.dto.DataDTO;
import com.roadmaker.f_hwangjinsang.service.airoadmaker.AIRoadMakerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/airoadmaker")
@RequiredArgsConstructor
public class AIRoadMakerController {

    // service 매칭
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
}
