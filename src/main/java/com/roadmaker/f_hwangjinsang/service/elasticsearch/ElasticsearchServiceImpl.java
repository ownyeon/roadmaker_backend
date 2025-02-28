package com.roadmaker.f_hwangjinsang.service.elasticsearch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ScoreSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchServiceImpl implements ElasticsearchService {
    // 엘라스틱 서치 직접 연결
    private final ElasticsearchClient client;

    @Override // MultiMatch 사용으로 Full-Text Search 사용
    public List<ElasticsearchDocument> search(String keyword) {
        try {
            // searchRequest 객체 생성
            log.info("서비스 searchRequest 객체 생성 진입");
            SearchRequest request = SearchRequest.of(s -> s
                    .index("destinations")
                    .query(q -> q
                            .multiMatch(m -> m
                                    .query(keyword)
                                    .fields("destiname^3", "destidesc^2", "destiaddress"))));
            SearchResponse<ElasticsearchDocument> searchResponse = client.search(request, ElasticsearchDocument.class);
            log.info("서비스 SearchResponse 진입");
            List<ElasticsearchDocument> documents = searchResponse.hits().hits()
                    .stream()
                    .map(Hit::source) // 검색 결과의 도큐먼트 본문 추출
                    .collect(Collectors.toList());
            log.info("서비스 SearchResponse 리턴 진입");
            return documents;

        } catch (Exception e) {
            throw new RuntimeException("Search failed", e); // 예외 구체화
        }
    }

    @Override // 검색
    public List<String> autocomplete(String keyword) {
        try {
            log.info("자동완성 요청");
            // match 요청 생성
            SearchRequest request = SearchRequest.of(s -> s
                    .index("destinations")
                    .size(5)
                    .query(q -> q
                            .match(m -> m
                                    .field("destiname")
                                    .query(keyword)))
                    .sort(List.of(
                            SortOptions.of(so -> so
                                    .score(ScoreSort.of(score -> score.order(SortOrder.Desc)))))));
            // 검색요청
            SearchResponse<ElasticsearchDocument> searchResponse = client.search(request, ElasticsearchDocument.class);

            // 자동완성 결과 추출
            List<String> suggestions = searchResponse.hits().hits()
                    .stream()
                    .limit(5)
                    .map(hit -> hit.source().getDestiname()) // destiname 반환
                    .collect(Collectors.toList());

            log.info("자동완성 결과 반환");
            return suggestions;
        } catch (Exception e) {
            log.info("자동완성 검색 실패" + e);
            throw new RuntimeException("자동완성 검색 실패", e);
        }
    }

}
