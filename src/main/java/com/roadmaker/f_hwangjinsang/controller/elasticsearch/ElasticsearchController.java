package com.roadmaker.f_hwangjinsang.controller.elasticsearch;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;
import com.roadmaker.f_hwangjinsang.service.elasticsearch.ElasticsearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/elastic")
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/search") // 여행지 관련 Full-Text Search
    public List<ElasticsearchDocument> searchSelect(@RequestParam String keyword) {
        log.info(keyword);
        return elasticsearchService.search(keyword);
    }

    @GetMapping("/autocomplete") // 자동 완성
    public List<String> searchAutocomplete(@RequestParam String keyword) {
        log.info(keyword);
        return elasticsearchService.autocomplete(keyword);
    }

}
