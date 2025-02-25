package com.roadmaker.f_hwangjinsang.service.admin;

import java.util.List;

import com.roadmaker.f_hwangjinsang.dto.DataDTO;
import com.roadmaker.f_hwangjinsang.dto.admin.AdminDTO;

public interface KeywordService {
    // 키워드 , 카테고리 전체검색
    public List<AdminDTO> getCategory();
    // 키워드 관리 입력 관련
    public boolean insertKeyword(AdminDTO admin);
    // 키워드 관리 수정 관련
    public boolean updateKeyword(AdminDTO admin);
    // 키워드 관리 삭제 관련
    public DataDTO deleteKeyword(AdminDTO admin);
    // 키워드 관리 키워드별 여행지리스트
    public List<AdminDTO> getKeywordByDestinationsList(AdminDTO admin);
    // 키워드 관리 여행지 추가 여행지 목록
    public List<AdminDTO> getDestinationList(AdminDTO admin);
}
