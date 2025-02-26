package com.roadmaker.a_common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataDTO <T> {

    private boolean success;
    private T data;
    private String message;
    
}
