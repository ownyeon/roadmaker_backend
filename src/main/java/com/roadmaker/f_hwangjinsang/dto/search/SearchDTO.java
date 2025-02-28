package com.roadmaker.f_hwangjinsang.dto.search;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO {
    private Integer memid, searchid;
    private String searchword;
    private Date searchdate;
}
