package com.roadmaker.f_hwangjinsang.service.admin;

import java.util.List;

import com.roadmaker.f_hwangjinsang.dto.admin.AdminDTO;

public interface AdminService {
    // 키워드 , 카테고리 전체검색
    public List<AdminDTO> getCategory();
}
