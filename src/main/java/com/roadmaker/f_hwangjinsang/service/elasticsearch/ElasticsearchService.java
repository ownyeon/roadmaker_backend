package com.roadmaker.f_hwangjinsang.service.elasticsearch;

import java.util.List;

import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;
import com.roadmaker.f_hwangjinsang.dto.search.SearchDTO;

public interface ElasticsearchService {
    // 멀티 쿼리 검색
    public List<ElasticsearchDocument> search(String keyword, Integer page);

    // 검색 자동완성
    public List<String> autocomplete(String keyword);

    // 검색어 저장
    public Boolean searchKeywordInsert(SearchDTO search);
}
