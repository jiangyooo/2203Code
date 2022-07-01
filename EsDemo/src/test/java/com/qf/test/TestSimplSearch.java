package com.qf.test;

import com.qf.util.EsClient;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.util.Map;

/**
 * ES简单查询
 * @author JiangBing
 * @date 2022/7/1 20:07
 */
public class TestSimplSearch {
    //获取和ES服务器的连接对象
    public RestHighLevelClient client = EsClient.getClient();

    //指定查询索引的名字
    private static final String INDEX = "book";

    /**
     * 根据term词元查询,也就是根据单词查询
     */
    @Test
    public void testTermQuery() throws Exception {
        //1.创建请求对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2.创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3.指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(5);

        //指定查询的域名和查询的单词
        sourceBuilder.query(QueryBuilders.termQuery("name","鬼吹灯"));

        //4.将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5.执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6.返回结果
        SearchHits hits = response.getHits();
        //查询到的总条数
        long count = hits.getTotalHits().value;
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取一条数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    /**
     * 根据多个词元进行查询
     */
    @Test
    public void testTermsQuery() throws Exception{
        //1.创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2.创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3.指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(10);

        //指定查询的域名和查询的单词
        sourceBuilder.query(QueryBuilders.termsQuery("name","鬼吹灯","金瓶梅"));

        //4.将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5.执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6.返回结果
        SearchHits hits = response.getHits();
        //查询到的总表数
        long value = hits.getTotalHits().value;
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }
    /**
     * 查询ES中所有的数据
     */
    @Test
    public void testMatchAll() throws Exception {
        //1.创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);
        //2.创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //3.指定查询对象
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(10);

        //指定查询的域名和查询的单词
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        //4.将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5.执行查询
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
            System.out.println(sourceAsMap);
        }
    }
    /**
     * 根据域名进行模糊的词汇查询
     */
    @Test
    public void testMatchQuery() throws Exception {
        //1.创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);
        //2.创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //3.指定查询对象
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(10);

        //指定查询的域名和查询的单词
        sourceBuilder.query(QueryBuilders.matchQuery("name","金瓶梅"));

        //4.将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5.执行查询
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
            System.out.println(sourceAsMap);
        }
    }

    /**
     * 根据多个关键词查询, 并且可以设置多个关键词的关系, 是and还是or
     */
    @Test
    public void testBooleanMatchQuery() throws Exception{
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3. 指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(5);

        //设置查询条件
        sourceBuilder.query(QueryBuilders.matchQuery("desc", "java 李瓶").operator(Operator.AND));

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
            System.out.println(sourceAsMap);
        }
    }

    /**
     * 从多个域中进行查询,也就是多个字段中查询
     */
    @Test
    public void testMultipartQuery() throws Exception{
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3. 指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(5);

        //设置查询条件
        sourceBuilder.query(QueryBuilders.multiMatchQuery("java", "name", "desc").operator(Operator.AND));

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
            System.out.println(sourceAsMap);
        }
    }
    /**
     * 根据主键id进行查询
     */
    @Test
    public void testFindById() throws Exception {
        //1. 创建请求对象
        GetRequest getRequest = new GetRequest(INDEX, "1");

        //2. 执行查询
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);

        //3. 输出结果
        System.out.println("========" + response.getSourceAsMap());
    }
    /**
     * 根据多个id进行查询
     * select * from 表名 where id in(?, ?,?)
     */
    @Test
    public void testFindByIds() throws Exception{
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3. 指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(5);

        //设置查询条件
        sourceBuilder.query(QueryBuilders.idsQuery().addIds("1", "2", "3"));

        //4. 将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5. 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6. 返回结果
        SearchHits hits = response.getHits();
        //查询到的总条数
        long count = hits.getTotalHits().value;
        System.out.println("=============" + count);
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取一条数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("========" + sourceAsMap);
        }
    }

    /**
     * 根据数字范围查询
     */
    @Test
    public void testFindByRange() throws Exception{
        //1. 创建请求对象
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //2. 创建查询条件对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //3. 指定查询条件
        //指定从第几条开始查询
        sourceBuilder.from(0);
        //指定每页查询多少条数据
        sourceBuilder.size(5);

        //设置查询条件
        sourceBuilder.query(QueryBuilders.rangeQuery("count").gte(20000).lte(50000000));

        //4. 将查询条件对象放入请求对象中
        searchRequest.source(sourceBuilder);

        //5. 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //6. 返回结果
        SearchHits hits = response.getHits();
        //查询到的总条数
        long count = hits.getTotalHits().value;
        System.out.println("=============" + count);
        //遍历查询到的结果集
        for (SearchHit hit : hits.getHits()) {
            //获取一条数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("========" + sourceAsMap);
        }
    }
}
