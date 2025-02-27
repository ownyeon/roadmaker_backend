package com.roadmaker.d_sindonggwon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.a_common.dto.DataDTO;
import com.roadmaker.d_sindonggwon.service.FestivalListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/festivallist")
public class FestivalListController {
    
    @Autowired
    private FestivalListService festivalListService;

    @PostMapping("/list")
    public DataDTO festivalListSelect() {
        
        DataDTO data = new DataDTO();
        try {
            data.setData(festivalListService.getFestivalList());
            data.setSuccess(true);
            data.setMessage("데이터 검색 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 검색 실패");
        }
        
        return data;
    }
}
