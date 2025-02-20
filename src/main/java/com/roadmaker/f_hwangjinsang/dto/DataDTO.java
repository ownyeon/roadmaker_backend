package com.roadmaker.f_hwangjinsang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {

    private boolean success;
    private Object data;
    private String message;
    
}
