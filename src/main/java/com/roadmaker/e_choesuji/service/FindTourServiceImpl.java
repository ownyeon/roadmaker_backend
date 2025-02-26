package com.roadmaker.e_choesuji.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.e_choesuji.dto.FindTourDTO;
import com.roadmaker.e_choesuji.mapper.FindTourMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FindTourServiceImpl implements FindTourService {

    @Autowired
    private FindTourMapper mapper;

    // 여행지 목록 조회
    @Override
    public List<FindTourDTO> getFindTourList() {
        log.info("서비스에서 받은 리스트 출력 :" + mapper.getFindTourList().toString());
        return mapper.getFindTourList();
    }

    // 여행지 상세조회
    public FindTourDTO getTourListDetail(Long destiid) {
        return mapper.getTourListDetail(destiid);
    }

    // 지역별 여행지 목록 조회
    public List<FindTourDTO> getTourListByKeywordAndRegion(String keyctg2, String keyword) {
        return mapper.getTourListByKeywordAndRegion(keyctg2, keyword);

    }
}
