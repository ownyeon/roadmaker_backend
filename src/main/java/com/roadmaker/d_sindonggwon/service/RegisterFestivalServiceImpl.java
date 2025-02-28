package com.roadmaker.d_sindonggwon.service;

import com.roadmaker.d_sindonggwon.dto.RegisterFestivalDTO;
import com.roadmaker.d_sindonggwon.mapper.RegisterFestivalMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterFestivalServiceImpl implements RegisterFestivalService {

    @Autowired
    private RegisterFestivalMapper festivalMapper;

    @Override
    public void registerFestival(RegisterFestivalDTO registerFestivalDTO) {
        try{
            log.info("Registering festival: {}", registerFestivalDTO);
            festivalMapper.registerFestival(registerFestivalDTO);
        }catch(Exception e){
            throw new RuntimeException("서비스단에서 오류발생 :" + e);
        }
        
    }
}
