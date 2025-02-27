package com.roadmaker.f_hwangjinsang.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roadmaker.a_common.dto.DataDTO;
import com.roadmaker.f_hwangjinsang.dto.admin.AdminDTO;
import com.roadmaker.f_hwangjinsang.mapper.admin.KeywordMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KeywordServiceImpl implements KeywordService {

    @Autowired
    private KeywordMapper mapper;

    @Override // 키워드 ,카테고리 전체 검색
    public List<AdminDTO> getCategory() {
        return mapper.getCategory();
    }

    @Override // 키워드 관리 입력
    public boolean insertKeyword(AdminDTO admin) {        
        return mapper.insertKeyword(admin) != 0 ? true : false;
    }

    @Override // 키워드 관리 수정
    public boolean updateKeyword(AdminDTO admin) {

        // 카테고리 수정
        if (admin.getChanged().equals("keyctg2")) {
            return mapper.updateKeyctg2(admin) != 0 ? true : false;
        } else if (admin.getChanged().equals("keyword")) {
            return mapper.updateKeyword(admin) != 0 ? true : false;
        }
        return false;
    }

    @Override // 키워드 관리 삭제
    public DataDTO deleteKeyword(AdminDTO admin) {
        DataDTO data = new DataDTO();
        try {
            if (admin.getKeymid() != null) {
                if (mapper.deleteKeyword(admin) != 0) {
                    data.setSuccess(true);
                    data.setMessage("데이터 삭제 성공");
                } else {
                    data.setSuccess(false);
                    data.setMessage("데이터 삭제 실패");
                }
            } else {
                log.info("country 개수파악 진입");
                Integer countryCount = mapper.countCountry(admin);
                log.info("country 개수는 " + countryCount);
                if (countryCount > 2) {
                    data.setSuccess(false);
                    data.setMessage("country가 1개보다 많습니다.");
                } else {
                    if (mapper.deleteCountry(admin) != 0) {
                        data.setSuccess(true);
                        data.setMessage("데이터 삭제 성공");
                    } else {
                        data.setSuccess(false);
                        data.setMessage("데이터 삭제 실패");
                    }
                }
            }
        } catch (Exception e) {
            data.setSuccess(false);
            data.setMessage("데이터 삭제 실패" + e.toString());
        }
        return data;
    }

    @Override // 키워드 관리 키워드별 여행지 리스트 검색
    public List<AdminDTO> getKeywordByDestinationsList(AdminDTO admin) {
        log.info("서비스 진입");
        return mapper.getKeywordByDestinationsList(admin);
    }

    @Override // 키워드 관리 여행지 추가 여행지 목록
    public List<AdminDTO> getDestinationList(AdminDTO admin) {
        if (admin.getSearchWord() != null) {
            return mapper.getDestinationListBySearchWord(admin);
        } else {
            return mapper.getDestinationList();
        }

    }

    @Override // 키워드 관리 키워드내 여행지 추가
    public boolean insertKeyused(AdminDTO admin) {
        AdminDTO dto = new AdminDTO();
        boolean isSuccess = false;
        try {
            if (admin.getSelectedDestinations().size() > 0) {
                for (Map<String, String> destination : admin.getSelectedDestinations()) {
                    dto.setDestiid(Integer.parseInt(destination.get("destiid")));
                    dto.setKeymid(admin.getKeymid());
                    Integer result = mapper.insertKeyused(dto);
                    isSuccess = result != 0 && result != -1;
                }
            }
            return isSuccess;
        } catch (Exception e) {
            log.info("데이터 입력중 서비스에서 오류발생" + e.toString());
            return false;
        }

    }

    @Override // 키워드 관리 키워드내 여행지 삭제
    public boolean deleteKeyused(AdminDTO admin) {
        return mapper.deleteKeyused(admin) != 0 ? true : false;
    }

}
