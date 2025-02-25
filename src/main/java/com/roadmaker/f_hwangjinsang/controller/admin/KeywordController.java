package com.roadmaker.f_hwangjinsang.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.a_common.dto.DataDTO;
import com.roadmaker.f_hwangjinsang.dto.admin.AdminDTO;
import com.roadmaker.f_hwangjinsang.service.admin.KeywordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class KeywordController {

    @Autowired
    private KeywordService service;

    // ******************************************************** 키워드 관련
    @PostMapping("/keyword/select") // 키워드 카테고리 전체 검색
    public DataDTO keywordSelect() {
        DataDTO data = new DataDTO();
        try {
            data.setSuccess(true);
            data.setData(service.getCategory());
            data.setMessage("데이터 검색 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 검색 실패");
        }

        return data;
    }

    @PostMapping("/keyword/insert") // 키워드 관리 입력 관련
    public DataDTO keywordInsert(@RequestBody AdminDTO admin) {
        DataDTO data = new DataDTO();

        try {
            data.setMessage("데이터 입력 성공");
            data.setSuccess(service.insertKeyword(admin));
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 입력 실패");
        }
        return data;
    }

    @PostMapping("/keyword/update") // 키워드 관리 수정 관련
    public DataDTO keywordUpdate(@RequestBody AdminDTO admin) {
        log.info(admin.toString());
        DataDTO data = new DataDTO();
        try {
            data.setMessage("데이터 수정 성공");
            data.setSuccess(service.updateKeyword(admin));
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 수정 실패");
        }
        return data;
    }

    @PostMapping("/keyword/delete") // 키워드 관리 삭제 관련
    public DataDTO keywordDelete(@RequestBody AdminDTO admin) {
        return service.deleteKeyword(admin);
    }

    @PostMapping("/keywordList/select") // 키워드 관리 여행지 검색 관련
    public DataDTO keywordListSelect(@RequestBody AdminDTO admin) {
        DataDTO data = new DataDTO();
        try {
            data.setMessage("데이터 검색 성공");
            data.setData(service.getKeywordByDestinationsList(admin));
            data.setSuccess(true);
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 검색 실패");
        }
        return data;
    }

    @PostMapping("/keyword/destinationList") // 키워드 관리 여행지 추가 여행지 리스트 검색
    public DataDTO destinationList(@RequestBody AdminDTO admin) {
        DataDTO data = new DataDTO();
        try {
            data.setMessage("데이터 검색 성공");
            data.setData(service.getDestinationList(admin));
            data.setSuccess(true);
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 검색 실패");
        }
        return data;
    }

    @PostMapping("/keyword/destinationAdd") // 키워드 관리 여행지 추가 여행지 리스트 검색
    public DataDTO destinationsInsert(@RequestBody AdminDTO admin) {
        log.info("" + admin);
        DataDTO data = new DataDTO();

        try {
            data.setSuccess(true);
            data.setData(service.insertKeyused(admin));
            data.setMessage("데이터 입력 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 입력 실패");
        }
        return data;
    }

    @PostMapping("/keyword/destinationDelete") // 키워드 관리 삭제 관련
    public DataDTO keywordByDestinationDelete(@RequestBody AdminDTO admin) {
        DataDTO data = new DataDTO();
        try {
            data.setSuccess(service.deleteKeyused(admin));
            data.setMessage("데이터 삭제 성공");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 삭제 실패");
        }
        return data;

    }
    // ******************************************************** 키워드 관련
}
