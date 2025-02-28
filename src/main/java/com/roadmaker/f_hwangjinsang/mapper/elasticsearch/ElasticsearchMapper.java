package com.roadmaker.f_hwangjinsang.mapper.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;

public interface ElasticsearchMapper extends ElasticsearchRepository<ElasticsearchDocument, Integer> {

}
