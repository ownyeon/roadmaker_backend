package com.roadmaker.d_sindonggwon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.d_sindonggwon.dto.FestivalListDTO;
import com.roadmaker.d_sindonggwon.mapper.FestivalListMapper;

@Service
public class FestivalListServiceImpl implements FestivalListService {

    @Autowired
    private FestivalListMapper festivalListMapper;

    public List<FestivalListDTO> getFestivalList() {
        return festivalListMapper.getFestivalList();
    }
    
}
