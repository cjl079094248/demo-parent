package com.demo.retrieve.controller;

import com.alibaba.fastjson.JSON;
import com.demo.utils.ResultUtilApp;
import com.demo.vo.ResultApp;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 要聚合的字段没有进行优化，也类似没有加索引。没有优化的字段es默认是禁止聚合/排序操作的。所以需要将要聚合的字段添加优化
 * 例如下面的排序字段id
 * curl -X PUT "localhost:9200/estest/_mapping?pretty" -H "Content-Type: application/json" -d"{\"properties\": {\"id\": { \"type\":     \"text\",\"fielddata\": true}}}"
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    /**
     * es写入示例
     * @param sno
     * @param sname
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultApp<Object> save(@RequestParam String sno, @RequestParam String sname, @RequestParam String id) throws IOException {
        Map<String,String> content = new HashMap<>();
        content.put("sno",sno);
        content.put("sname",sname);
        IndexRequest indexRequest = new IndexRequest("estest")
                .id(id)
                .source(content);
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        return ResultUtilApp.success("插入到es成功");
    }

    /**
     * 更新数据
     * @param sno
     * @param sname
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultApp<Object> update(@RequestParam String sno, @RequestParam String sname, @RequestParam String id) throws IOException {
        Map<String,String> content = new HashMap<>();
        content.put("sno",sno);
        content.put("sname",sname);
        BulkRequest request = new BulkRequest();
        //request.add(new UpdateRequest("estest", id).doc(JSON.toJSONString(content), XContentType.JSON));
        request.add(new UpdateRequest("estest", id).doc(JSON.toJSONString(content), XContentType.JSON).upsert(JSON.toJSONString(content), XContentType.JSON));
        client.bulk(request, RequestOptions.DEFAULT);
        return ResultUtilApp.success("更新es成功");
    }

    /**
     * es查询示例
     * @param keyWord
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/search", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultApp<Object> search(@RequestParam String keyWord) throws IOException {
        String indexName = "estest";
        String keyWordName = "sno";
        String sortName = "id";
        SearchRequest searchRequest = new SearchRequest(indexName);//设置查询索引
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder mqb = QueryBuilders.matchQuery(keyWordName, keyWord);//匹配字段
        //默认排序条件
        FieldSortBuilder fsb = SortBuilders.fieldSort(sortName).order(SortOrder.ASC);
        searchSourceBuilder.query(mqb)
                .sort(new ScoreSortBuilder()) //匹配度排序
                .sort(fsb)
                .size(999);//此处需要将返回值设置为最大
        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(new HighlightBuilder.Field(keyWordName));
        //关闭多次高亮
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);//查
        return ResultUtilApp.data(searchResponse);
    }
}
