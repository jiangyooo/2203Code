package com.qf.test;

import com.qf.util.EsClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.mapper.RangeFieldMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author JiangBing
 * @date 2022/7/4 9:26
 */
public class TestHardSearch {

    //获取和ES服务器的连接对象
    public RestHighLevelClient client = EsClient.getClient();

    //指定查询索引的名字
    private static final String INDEX = "book";

    @Test
    public void TestBooleanQuery() throws IOException {

        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3. 指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(5);

        //创建组合查询对象
        //must  必须的意思相当于and
        //mustNot 不必须, 相当于not
        //should  或者, 相当于or
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //根据名字进行查询, 名字叫做金瓶梅
        boolQuery.must().add(QueryBuilders.termsQuery("name", "金瓶梅"));
        //根据描述域进行查询, 描述中含有李瓶
        boolQuery.must().add(QueryBuilders.matchQuery("desc", "李瓶"));

        //将组合查询条件放入查询对象中
        sourceBuilder.query(boolQuery);

        //4. 将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5. 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6. 返回结果
        SearchHits hits = response.getHits();
        //查询到的总条数
        long count = hits.getTotalHits().value;
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取一条数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("========" + sourceAsMap);
        }

    }

    /**
     * 过滤查询:提高查询准其额度,越查询数据越精确
     * 根据关键词查询
     * 根据锅里条件在上一次查询结果中进行过滤
     */
    @Test
    public void TestFilterQuery() throws IOException {
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3. 指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(5);

        //创建组合查询对象
        //must  必须的意思相当于and
        //mustNot 不必须, 相当于not
        //should  或者, 相当于or
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //根据名字进行查询, 名字叫做金瓶梅
        boolQuery.must().add(QueryBuilders.termsQuery("name", "鬼吹灯"));
        //根据描述域进行查询, 描述中含有李瓶
        boolQuery.filter(QueryBuilders.termQuery("desc", "盗墓"));

        //将组合查询条件放入查询对象中
        sourceBuilder.query(boolQuery);

        //4. 将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5. 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6. 返回结果
        SearchHits hits = response.getHits();
        //查询到的总条数
        long count = hits.getTotalHits().value;
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取一条数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("========" + sourceAsMap);
        }
    }

    /**
     * 高亮查询:搜索的关键字,在查询结果的名字中会变成红色醒目展示
     */
    @Test
    public void testHighLightQuery() throws Exception{
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();



        //根据名字进行查询, 名字叫做金瓶梅
        sourceBuilder.query(QueryBuilders.matchQuery("name", "鬼吹灯"));
        //设置高亮
        HighlightBuilder highlight = new HighlightBuilder();
        highlight.field("name").postTags("<span style='color:red'>").postTags("</span>");

        //将高亮对象放入查询条件对象中
        sourceBuilder.highlighter(highlight);

        //4. 将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5. 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6. 返回结果
        SearchHits hits = response.getHits();
        //查询到的总条数
        long count = hits.getTotalHits().value;
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取一条数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            //获取高亮度的名字字段结果
            HighlightField highlightField = hit.getHighlightFields().get("name");
            String string = highlightField.fragments()[0].string();
            System.out.println("***********************" +string);

            //将高亮的名字,放入这条数据的那么属性中,覆盖原来的数据
            sourceAsMap.put("name", string);
            System.out.println("*/******************" + sourceAsMap);
        }
    }

    /**
     * 聚合查询,count算总数
     * @throws Exception
     */
    @Test
    public void testCountQuery() throws Exception{
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //查询条件
        sourceBuilder.query(QueryBuilders.matchQuery("name","鬼吹灯"));

        //设置聚合查询条件
        sourceBuilder.aggregation(AggregationBuilders.cardinality("aggCount").field("count"));

        //4.将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5.执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //6.返回结果
        SearchHits hits = response.getHits();
        //查询得到的总条数
        long value = hits.getTotalHits().value;
        //遍历结果
        for (SearchHit hit : hits.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("=========" + sourceAsMap);
        }
        //遍历获取聚合统计结果并打印
        Cardinality aggCount = response.getAggregations().get("aggCount");
        long countValue = aggCount.getValue();
        System.out.println(countValue);
    }
    /**
     * 根据文章的字数,范围进行统计
     */
    @Test
    public void testRangeCountQuery() throws Exception{
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //设置聚合查询条件,cardinality里面是给当前统计起个名字, 后面获取结果的时候, 根据这个名字获取
        sourceBuilder.aggregation(AggregationBuilders.range("aggCount")
                .field("count")
                .addUnboundedTo(100000)
                .addRange(100000,300000)
                .addUnboundedFrom(300000)
        );
        //4. 将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5. 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6. 返回结果
        SearchHits hits = response.getHits();
        //查询到的总条数
        long count = hits.getTotalHits().value;
        System.out.println(count);
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取一条数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //打印结果
            System.out.println("========" + sourceAsMap);
        }

        //遍历聚合结果并打印
        Range aggCount = response.getAggregations().get("aggCount");
        for (Range.Bucket  bucket : aggCount.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            Object from = bucket.getFrom();
            Object to = bucket.getTo();
            long docCount = bucket.getDocCount();
            System.out.println("===key=====" + keyAsString);
            System.out.println("===from=====" + from);
            System.out.println("====to====" + to);
            System.out.println("====docCount====" + docCount);
            System.out.println("=====================================================");
        }
    }

    @Test
    public void testDeleteByQuery() throws Exception {

        //1. 创建删除请求对象
        DeleteByQueryRequest request = new DeleteByQueryRequest(INDEX);

        //2. 设置查询条件
        request.setQuery(QueryBuilders.matchQuery("name", "鬼吹灯"));

        //3. 执行删除
        BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);

        //4. 返回结果
        System.out.println("===========" + response);
    }
}
