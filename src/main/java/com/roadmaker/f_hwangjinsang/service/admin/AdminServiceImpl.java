package com.roadmaker.f_hwangjinsang.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.f_hwangjinsang.dto.admin.AdminDTO;
import com.roadmaker.f_hwangjinsang.mapper.admin.AdminMapper;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper mapper;

    @Override // 키워드 ,카테고리 전체 검색
    public List<AdminDTO> getCategory() {
        return mapper.getCategory();
    }

}
