package com.qf.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author JiangBing
 * @date 2022/7/1 11:24
 */
public class EsClient {

    /**
     * 获取和ES服务器的连接
     * @return
     */
    public static RestHighLevelClient getClient() {
        //1. 创建和服务器的连接
        HttpHost httpHost = new HttpHost("192.168.200.129", 9200);
        //2. 创建RestClient对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        //3. 创建返回可以使用的连接对象
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

}
