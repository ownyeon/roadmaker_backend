package com.roadmaker.e_choesuji.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.e_choesuji.dto.FindTourDTO;
import com.roadmaker.e_choesuji.dto.HotelRegistrationRequestDTO;
import com.roadmaker.e_choesuji.mapper.FindTourMapper;

import io.jsonwebtoken.lang.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FindTourServiceImpl implements FindTourService {

    private final FindTourMapper mapper;

    @Autowired
    FindTourServiceImpl(FindTourMapper mapper) {
        this.mapper = mapper;
    }

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

    // DB에서 지역 목록을 조회
    public List<String> getRegions() {
        return mapper.getRegions(); // DB에서 지역 목록을 가져오는 메서드 호출
    }

    // 지역과 키워드로 여행지 목록 조회
    public List<FindTourDTO> getTourListByKeywordAndRegion(String keyctg2, String keyword) {
        return mapper.getTourListByKeywordAndRegion(keyctg2, keyword);
    }

    // 신규여행지 등록
    public void registerHotel(HotelRegistrationRequestDTO request) throws Exception {
        try {
            log.info("호텔 등록 처리 시작");

            // Mapper 호출하여 DB에 저장
            mapper.insertDestination(request); // DB에 데이터 삽입

            log.info("호텔 등록 정보: {}", request);
            log.info("호텔 등록이 완료되었습니다.");
        } catch (Exception e) {
            log.error("호텔 등록 중 오류 발생", e);
            throw new Exception("호텔 등록 중 오류가 발생했습니다.", e);
        }
    }

}
