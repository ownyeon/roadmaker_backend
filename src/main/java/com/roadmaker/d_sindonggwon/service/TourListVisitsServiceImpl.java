package com.roadmaker.d_sindonggwon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.d_sindonggwon.mapper.TourListVisitsMapper;

@Service
public class TourListVisitsServiceImpl implements TourListVisitsService {

    @Autowired
    private TourListVisitsMapper tourListVisitsMapper;

    @Override
    public void updateViews(int destiid) {
        tourListVisitsMapper.updateViews(destiid);
    }

    @Override
    public int getViews(int destiid) {
        return tourListVisitsMapper.getViews(destiid);
        
    }
    
}
