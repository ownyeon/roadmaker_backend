package com.roadmaker.f_hwangjinsang.service.elasticsearch;

import java.util.List;

import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;

public interface ElasticsearchService {
    // 제목 검색
    public List<ElasticsearchDocument> search(String keyword);
}
