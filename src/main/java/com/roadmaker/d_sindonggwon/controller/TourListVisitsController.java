package com.roadmaker.d_sindonggwon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.a_common.dto.DataDTO;
import com.roadmaker.d_sindonggwon.service.TourListVisitsService;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/tourlist")
public class TourListVisitsController {
    
    @Autowired
    private TourListVisitsService tourListVisitsService;
    
    // 조회수 증가
    @PutMapping("/{destiid}/view")
    public String updateViews(@PathVariable int destiid) {
        tourListVisitsService.updateViews(destiid);
        return "Views updated!";
        
    }

    // 조회수 확인
    @GetMapping("/{destiid}/view")
    public DataDTO getViews(@PathVariable int destiid) {
        DataDTO data = new DataDTO();
        int views = tourListVisitsService.getViews(destiid);  // 조회수 가져오기
        data.setData(views);  // 조회수 값을 DataDTO에 담기
        return data;
    }
    
}
