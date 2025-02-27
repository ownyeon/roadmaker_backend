package com.roadmaker.e_choesuji.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.a_common.dto.DataDTO;//공용으로 쓰기로 함 
import com.roadmaker.e_choesuji.dto.FindTourDTO;
import com.roadmaker.e_choesuji.dto.HotelRegistrationRequestDTO;
import com.roadmaker.e_choesuji.service.FindTourService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/findtour")
public class FindTourController {

    // service 매칭
    @Autowired
    private FindTourService findTourService;

    // 여행지 리스트 불러오기
    @GetMapping("/tourlist")
    public DataDTO findtourListSelect() {

        DataDTO data = new DataDTO();
        log.info("컨트롤러 진입 완료 to");
        try {
            data.setData(findTourService.getFindTourList());
            data.setSuccess(true);
            data.setMessage("데이터 검색 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 검색 실패");
        }

        return data;
    }

    // 여행지 상세정보 불러오기
    @GetMapping("/detail/{destiid}")
    public DataDTO tourListDetailSelect(@PathVariable Long destiid) {
        DataDTO data = new DataDTO();
        try {
            FindTourDTO detail = findTourService.getTourListDetail(destiid);
            data.setData(detail);
            data.setSuccess(true);
            data.setMessage("상세 정보 조회 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("상세 정보 조회 실패");
        }
        return data;
    }

    // 여행지 지역키워드 불러오기
    @GetMapping("/tourlistdetailselect")
    public List<String> getRegions() {
        return findTourService.getRegions();  // 지역 목록을 반환하는 서비스 호출
    }

    // 지역과 키워드에 맞는 여행지 목록 조회 API
    @GetMapping("/search")
    public List<FindTourDTO> searchTours(
        @RequestParam String keyctg2,    // 지역
        @RequestParam String keyword    // 키워드
    ) {
        return findTourService.getTourListByKeywordAndRegion(keyctg2, keyword);
    }
   
    


    //여행지신규등록록
    @PostMapping("/register")
    public DataDTO registerHotel(@RequestBody HotelRegistrationRequestDTO request) {
        DataDTO data = new DataDTO();
        try {
            log.info("신규 관광정보 등록 시작");
            findTourService.registerHotel(request); // 서비스 호출
            data.setSuccess(true);
            data.setMessage("등록이 완료되었습니다.");
        } catch (Exception e) {
            log.error("신규 관광정보 등록 중 오류 발생", e);
            data.setSuccess(false);
            data.setMessage("등록 중 오류가 발생했습니다.");
        }
        return data;
    }

}
