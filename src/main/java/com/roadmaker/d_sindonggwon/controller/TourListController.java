package com.roadmaker.d_sindonggwon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.a_common.dto.DataDTO;
import com.roadmaker.d_sindonggwon.dto.TourListDetailDTO;
import com.roadmaker.d_sindonggwon.service.TourListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/tourlist")
public class TourListController {

    // service 매칭
    @Autowired
    private TourListService tourListService;

    // 여행지 리스트 불러오기
    @PostMapping("/list")
    public DataDTO tourListSelect() {
        
        DataDTO data = new DataDTO();
        try {
            data.setData(tourListService.getTourList());
            data.setSuccess(true);
            data.setMessage("데이터 검색 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 검색 실패");
        }
        
        return data;
    }

    // 여행지 상세정보 불러오기
    @GetMapping("/detail/{destiid}")
    public DataDTO tourListDetailSelect(@PathVariable Long destiid) {
        DataDTO data = new DataDTO();
        try {
            TourListDetailDTO detail = tourListService.getTourListDetail(destiid);
            data.setData(detail);
            data.setSuccess(true);
            data.setMessage("상세 정보 조회 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("상세 정보 조회 실패");
        }
        return data;
    }
}
