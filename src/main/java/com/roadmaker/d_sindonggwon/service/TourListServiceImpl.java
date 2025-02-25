package com.roadmaker.d_sindonggwon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.d_sindonggwon.dto.TourListDTO;
import com.roadmaker.d_sindonggwon.dto.TourListDetailDTO;
import com.roadmaker.d_sindonggwon.mapper.TourListMapper;

@Service
public class TourListServiceImpl implements TourListService {

    @Autowired
    private TourListMapper mapper;
    
    @Override
    public List<TourListDTO> getTourList() {
        return mapper.getTourList();
    }

    @Override
    public TourListDetailDTO getTourListDetail(Long destiid) {
        return mapper.getTourListDetail(destiid);
    }
    
}
