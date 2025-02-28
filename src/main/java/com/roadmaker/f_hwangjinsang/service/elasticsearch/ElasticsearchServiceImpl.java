package com.roadmaker.f_hwangjinsang.service.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.roadmaker.f_hwangjinsang.dto.elasticsearch.ElasticsearchDocument;
import com.roadmaker.f_hwangjinsang.dto.search.SearchDTO;
import com.roadmaker.f_hwangjinsang.mapper.search.SearchMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ScoreSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
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

        private final SearchMapper mapper;

        @Override // MultiMatch 사용으로 Full-Text Search 사용
        @Async
        public List<ElasticsearchDocument> search(String keyword, Integer page) {
                try {
                        Integer from = (page - 1) * 10;
                        Integer size = 10;

                        // 1. 정확하게 일치하는 쿼리 (term 쿼리 사용)
                        SearchRequest exactMatchRequest = SearchRequest.of(s -> s
                                        .index("destinations")
                                        .query(q -> q
                                                        .term(t -> t
                                                                        .field("destiname.keyword")
                                                                        .value(keyword)
                                                                        .caseInsensitive(true)))
                                        .size(size));
                        // 2. 유사 경색 쿼리
                        SearchRequest fuzzyMatchRequest = SearchRequest.of(s -> s
                                        .index("destinations")
                                        .query(q -> q
                                                        .bool(b -> b
                                                                        .should(sh -> sh
                                                                                        .matchPhrase(mp -> mp
                                                                                                        .query(keyword)
                                                                                                        .field("destiname")
                                                                                                        .boost(5.0f)))
                                                                        .should(sh -> sh
                                                                                        .match(m -> m
                                                                                                        .query(keyword)
                                                                                                        .field("destiname")
                                                                                                        .boost(3.0f)))
                                                                        .should(sh -> sh
                                                                                        .multiMatch(m -> m
                                                                                                        .query(keyword)
                                                                                                        .fields("destiname",
                                                                                                                        "destidesc^15")
                                                                                                        .type(TextQueryType.BestFields)))
                                                                        .minimumShouldMatch("1")))
                                        .from(from)
                                        .size(size));
                        // 먼저 일치하는 쿼리 실행
                        SearchResponse<ElasticsearchDocument> exactMatchResponse = client.search(exactMatchRequest,
                                        ElasticsearchDocument.class);
                        List<ElasticsearchDocument> exactMatches = exactMatchResponse.hits().hits()
                                        .stream()
                                        .map(Hit::source)
                                        .collect(Collectors.toList());

                        // 먼저 일치하는 결과가 size 보다 적으면 나머지를 유사 검색으로 채움
                        List<ElasticsearchDocument> result = new ArrayList<>(exactMatches);

                        if (exactMatches.size() < size) {
                                // 이미 찾은 매치를 제외
                                List<String> exactMatchIds = exactMatches.stream()
                                                .map(doc -> String.valueOf(doc.getDestiid()))
                                                .collect(Collectors.toList());

                                // 유사 검색 쿼리에 이미 찾은 문서 제거
                                if (!exactMatchIds.isEmpty()) {
                                        fuzzyMatchRequest = SearchRequest.of(s -> s
                                                        .index("destinations")
                                                        .query(q -> q
                                                                        .bool(b -> b
                                                                                        .should(sh -> sh
                                                                                                        .matchPhrase(mp -> mp
                                                                                                                        .query(keyword)
                                                                                                                        .field("destiname")
                                                                                                                        .boost(5.0f)))
                                                                                        .should(sh -> sh
                                                                                                        .match(m -> m
                                                                                                                        .query(keyword)
                                                                                                                        .field("destiname")
                                                                                                                        .boost(3.0f)))
                                                                                        .should(sh -> sh
                                                                                                        .multiMatch(m -> m
                                                                                                                        .query(keyword)
                                                                                                                        .fields("destiname",
                                                                                                                                        "destidesc^15")
                                                                                                                        .type(TextQueryType.BestFields)))
                                                                                        .mustNot(mn -> mn
                                                                                                        .ids(ids -> ids
                                                                                                                        .values(exactMatchIds)))
                                                                                        .minimumShouldMatch("1")))
                                                        .from(0)
                                                        .size(size - exactMatches.size()));
                                }

                                SearchResponse<ElasticsearchDocument> fuzzyResponse = client.search(fuzzyMatchRequest,
                                                ElasticsearchDocument.class);
                                List<ElasticsearchDocument> fuzzyMatches = fuzzyResponse.hits().hits()
                                                .stream()
                                                .map(Hit::source)
                                                .collect(Collectors.toList());

                                // 결과 합치기
                                result.addAll(fuzzyMatches);
                        }
                        return result;
                } catch (Exception e) {
                        log.error("Search failed", e);
                        throw new RuntimeException("Search failed: " + e.getMessage(), e);
                }
        }

        @Override // 자동완성
        @Async
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
                                                                        .score(ScoreSort.of(score -> score
                                                                                        .order(SortOrder.Desc)))))));
                        // 검색요청
                        SearchResponse<ElasticsearchDocument> searchResponse = client.search(request,
                                        ElasticsearchDocument.class);

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

        @Override
        @Async
        public Boolean searchKeywordInsert(SearchDTO search) {
                try {
                        if (mapper.searchKeywordInsert(search) != 0) {
                                return true;
                        } else {
                                return false;
                        }
                } catch (Exception e) {
                        throw new RuntimeException("검색어 저장중 오류 발생", e);
                }

        }

}
