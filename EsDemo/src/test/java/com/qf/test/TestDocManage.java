package com.qf.test;

/**
 * @author JiangBing
 * @date 2022/7/1 17:24
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qf.pojo.Persion;
import com.qf.util.EsClient;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * es数据修改
 */
public class TestDocManage {

    //创建和ES服务器连接对象
    private RestHighLevelClient client = EsClient.getClient();

    //索引名字
    private static final String INDEX = "persion";


    /**
     * 创建文档,文档相当于数据库表中的一条数据
     */
    @Test
    public void testCreateDocument() throws Exception {
        //1.创建一个实体类对象
        Persion persion = new Persion("1", "无涯", 20, new Date());

        //2.将实体类转成json格式数据
        String jsonString = JSON.toJSONString(persion);

        //3.准备一个请求对象,指定操作的索引的名字
        IndexRequest request = new IndexRequest(INDEX);

        //4.手动指定这个文档的id,,将JSON数据放入这个请求对象中
        request.id(persion.getId()).source(jsonString, XContentType.JSON);

        //5.通过client对象执行添加
        IndexResponse index = client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 修改文档
     */
    @Test
    public void testUpdateDocument() throws Exception {
        //1.准备一个实体类数据,这个数据就是需要修改的内容
        Persion persion = new Persion("1", "天涯", 200, new Date());

        //2.将实体类转换成JSON格式
        String jsonString = JSON.toJSONString(persion);

        //3.创建请求对象,指定索引的名字,指定被修稿的文档的id
        UpdateRequest updateRequest = new UpdateRequest(INDEX, persion.getId());

        //4.设置将数据修改为什么修容
        updateRequest.doc(jsonString, XContentType.JSON);

        //5.执行请求,返回响应
        UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
    }

    /**
     * 删除文档数据
     */
    @Test
    public void testDeleteDocument() throws Exception {
        //1.创建一个删除请求对象,第一参数:索引名字,第二参数:被删除的文档的id
        DeleteRequest request = new DeleteRequest(INDEX, "1");

        //2.执行请求删除
        client.delete(request, RequestOptions.DEFAULT);
    }

}
