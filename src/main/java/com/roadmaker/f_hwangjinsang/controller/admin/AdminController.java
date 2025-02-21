package com.roadmaker.f_hwangjinsang.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.f_hwangjinsang.dto.DataDTO;
import com.roadmaker.f_hwangjinsang.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService service;

    @PostMapping("/keyword/select") // 키워드 카테고리 전체 검색
    public DataDTO keywordSelect() {

        DataDTO data = new DataDTO();
        try {
            data.setSuccess(true);
            data.setData(service.getCategory());
            data.setMessage("데이터 검색 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 검색 실패");
        }

        return data;
    }

}
