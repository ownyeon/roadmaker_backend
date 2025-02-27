package com.roadmaker.d_sindonggwon.service;

import java.util.List;

import com.roadmaker.d_sindonggwon.dto.FestivalListDTO;

public interface FestivalListService {  
    // 축제 리스트 불러오기
    public List<FestivalListDTO> getFestivalList();
    
}
