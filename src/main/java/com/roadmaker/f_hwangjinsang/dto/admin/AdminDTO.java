package com.roadmaker.f_hwangjinsang.dto.admin;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    // 기타관련
    private String searchWord;
    private List<Map<String, String>> selectedDestinations;
    // 코스관련
    private Integer keyuid;
    // 키워드 관련
    private Integer keymid;
    private String keyctg1, keyctg2, keyword, afterValue, changed;
    // 여행지 관련
    private Integer destiid, desticrawlid;
    private String destiname, destidesc, destiaddress, destiholid, destiopenhr, destiparkavail, desticontact,
            destiregdate, destiappear, imgsname1, imgsname2, imgsname3, imgsname4, imgfname1, imgfname2, imgfname3,
            imgfname4;
    private Double destilongit, destilatit;
}
