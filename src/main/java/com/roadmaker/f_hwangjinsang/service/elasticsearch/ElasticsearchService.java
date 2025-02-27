package com.roadmaker.f_hwangjinsang.service.elasticsearch;

import java.util.List;

import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;

public interface ElasticsearchService {
    // 멀티 쿼리 검색
    public List<ElasticsearchDocument> search(String keyword);

    // 검색 자동완성
    public List<String> autocomplete(String keyword);
}
