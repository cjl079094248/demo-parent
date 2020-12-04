package com.demo.retrieve.common.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ElasticSearchConfig implements EnvironmentAware {
    private Environment env;

    @Value("${es.node1-url}")
    private String esUrl1;
    @Value("${es.node2-url}")
    private String esUrl2;
    @Value("${es.node3-url}")
    private String esUrl3;
    @Value("${es.node1-port}")
    private Integer esPort1;
    @Value("${es.node2-port}")
    private Integer esPort2;
    @Value("${es.node3-port}")
    private Integer esPort3;

    /**
     * 引入ElasticSearch client
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esUrl1, esPort1, "http"),
                        new HttpHost(esUrl2, esPort2, "http"),
                        new HttpHost(esUrl3, esPort3, "http")));
        return client;

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env=environment;
    }
}
