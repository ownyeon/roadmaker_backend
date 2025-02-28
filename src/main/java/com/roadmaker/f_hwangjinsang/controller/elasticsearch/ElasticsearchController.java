package com.roadmaker.f_hwangjinsang.controller.elasticsearch;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roadmaker.aop.RunningTime;
import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;
import com.roadmaker.f_hwangjinsang.dto.search.SearchDTO;
import com.roadmaker.f_hwangjinsang.service.elasticsearch.ElasticsearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/elastic")
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @RunningTime
    @GetMapping("/search") // 여행지 관련 Full-Text Search
    public List<ElasticsearchDocument> searchSelect(@RequestParam String keyword, @RequestParam Integer page) {
        return elasticsearchService.search(keyword, page);
    }

    @RunningTime
    @GetMapping("/autocomplete") // 자동 완성
    public List<String> searchAutocomplete(@RequestParam String keyword) {
        return elasticsearchService.autocomplete(keyword);
    }

    @RunningTime
    @PostMapping("/search") // 검색어 저장
    public void searchWordSave(@RequestBody SearchDTO search) {
        elasticsearchService.searchKeywordInsert(search);

    }

}
