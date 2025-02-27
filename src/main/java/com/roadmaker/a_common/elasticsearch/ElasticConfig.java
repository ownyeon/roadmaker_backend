package com.roadmaker.a_common.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticConfig extends ElasticsearchConfiguration {

    //application에 설정한것 불러오기
    @Value("${spring.elasticsearch.uris}")
    private String elasticUrl;

    @Override
    public ClientConfiguration clientConfiguration() {
         // ClientConfiguration 설정
        return ClientConfiguration.builder()
                .connectedTo(elasticUrl)// Elasticsearch 연결 URL
                .build();// 최종 설정 반환
    }

}
