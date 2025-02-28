package com.roadmaker.d_sindonggwon.controller;

import com.roadmaker.d_sindonggwon.dto.RegisterFestivalDTO;
import com.roadmaker.d_sindonggwon.service.RegisterFestivalService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/festival")
public class RegisterFestivalController {

    @Autowired
    private RegisterFestivalService registerFestivalService;

    @PostMapping("/register")
    public String registerFestival(@RequestBody RegisterFestivalDTO registerFestivalDTO) {
        log.info("Received Festival DTO: {}", registerFestivalDTO);

        // 여기서 DTO의 Date 값을 사용하면 됩니다.
        
        try {
            // 서비스로 넘기기 전에 데이터를 필요한 형태로 가공할 수 있습니다.
            registerFestivalService.registerFestival(registerFestivalDTO);
            return "Festival registration successful!";
        } catch (Exception e) {
            return "Error occurred: " + e.getMessage();
        }
    }
}
