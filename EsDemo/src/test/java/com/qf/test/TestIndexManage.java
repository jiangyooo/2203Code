package com.qf.test;

import com.qf.util.EsClient;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.DeleteAliasRequest;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;

import java.io.IOException;

/**
 * @author JiangBing
 * @date 2022/7/1 11:25
 */
public class TestIndexManage {

    private RestHighLevelClient client = EsClient.getClient();

    private static final String INDEX_NAME = "persion";

    /**
     * 创建索引:"包括因索引的主分片,备份分片,还有文档结构
     */
    @Test
    public void testCreatIndex() throws IOException {

        //1.创建主分片和备份分片
        Settings.Builder settings = Settings.builder()
                .put("number_of_shards", 5)
                .put("number_of_replicas", 1);

        //2.创建索引结构,包括域名和于的类型
        XContentBuilder xContentBuilder = JsonXContent.contentBuilder()
                .startObject()
                        .startObject("properties")
                            .startObject("name")
                                .field("type", "text")
                                .field("analyzer","ik_max_word")
                            .endObject()

                            .startObject("age")
                                .field("type","integer")
                            .endObject()

                            .startObject("birthday")
                                .field("type","date")
                                .field("format","yyyy-MM-dd")
                            .endObject()
                    .endObject()
                .endObject();
        //3.将索引结构和索引的分片设置封装到一个请求对线对象农
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        //将分片设置放入请求对象中
        request.settings(settings);
        //将结构设置放入请求对象中
        request.mapping(xContentBuilder);


        //4.像es服务器发送请求执行创建操作
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response + "***********************************");
    }


    @Test
    public void testIndexExists() throws Exception {
        //1.创建Get请求对象
        GetAliasesRequest request = new GetAliasesRequest();

        //2.将索引名字放入请求对象中
        request.indices(INDEX_NAME);

        //3.查询
        boolean flag = client.indices().existsAlias(request, RequestOptions.DEFAULT);

        //4.返回结果输出
        System.out.println(flag + "**********************");
    }

    @Test
    public void testDeleteIndex() throws Exception {
        //1.创建一个删除的请求对象
        DeleteIndexRequest request = new DeleteIndexRequest();

        //2.指定需要删除的索引名字
        request.indices(INDEX_NAME);

        //3.发出请求删除
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);

        //4.返回结果打印
        System.out.println(delete);

    }
}
