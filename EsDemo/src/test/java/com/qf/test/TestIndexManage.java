package com.qf.test;

import com.qf.util.EsClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.json.JsonXContent;
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
                .startObject("")
                .startObject("")
                .endObject()
                .endObject()
                .endObject();
        //3.将索引结构和索引的分片设置封装到一个请求对线对象农

        //4.像es服务器发送请求执行创建操作

    }

}
