package com.demo.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;

import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;

public class AsyncHttpClientUtil {
    //异步httpclient
    private CloseableHttpAsyncClient httpAsyncClient;
    //网络环境配置器
    private volatile RequestConfig config;

    private static AsyncHttpClientUtil instance;

    public static synchronized AsyncHttpClientUtil getInstance() {
        if (instance == null) {
            instance = new AsyncHttpClientUtil();
        }
        return instance;
    }

    private AsyncHttpClientUtil(){
        try {
            init();
        }catch (Exception ex){
            //todo 改为记errorlog

        }
    }

    public String  HttpPostWithParam(String url,String data){
        CloseableHttpAsyncClient httpAsyncClient = getHttpClient();

        //创建post方式请求对象，向指定的url发送一次异步post请求
        HttpPost httpPost = new HttpPost(url); //请求Abc的URL
        httpPost.setConfig(config);
        // set header
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");
        Map<String, String> params = new HashMap<>();
        params.put("data", data);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(params),"utf-8"));

        try{
            //执行请求操作，并拿到结果（异步）
            //回调
            FutureCallback<HttpResponse> callback = new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse result) {
                    //成功无须理会
                }
                @Override
                public void failed(Exception e) {
                    //todo 改为记errorlog

                }
                @Override
                public void cancelled() {
                    //todo 改为记errorlog
                }
            };
            httpAsyncClient.execute(httpPost,callback);
        }catch(Exception e){
            //记录异常日志
            //todo 改为记errorlog

        }
        return "";
    }

    /**
     * 获取httpclient
     */
    private CloseableHttpAsyncClient getHttpClient() {
        if (null == this.httpAsyncClient) {
            for (int i = 0; i < 3; i++) {
                try {
                    init();
                } catch (Exception e) {
                    //todo 改为记errorlog
                }
            }
        }
        return this.httpAsyncClient;
    }

    private void init() throws Exception {
        if (null != this.httpAsyncClient) {
            return;
        }
        refresh();
    }

    public synchronized void refresh() {
        Integer timeout = 300;
        this.config = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).build();
        try {
            this.httpAsyncClient = initAsynHttpClient(timeout, true);
        } catch (Exception e) {
            //todo 改为记errorlog
            this.httpAsyncClient = null;
        }
    }

    public static CloseableHttpAsyncClient initAsynHttpClient(int timeout, boolean isSetTimeout) throws Exception {
        // 设置协议http对应的处理socket链接工厂的对象
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy> create().register("http", NoopIOSessionStrategy.INSTANCE).build();

        // 设置连接池大小
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        // 初始化连接池
        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor, sessionStrategyRegistry);
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(80000).build();
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();

        connectionManager.setDefaultConnectionConfig(connectionConfig);

        // 设置连接池的属性
        connectionManager.setMaxTotal(500); // 连接池最大数
        connectionManager.setDefaultMaxPerRoute(500); // 每个路由基础连接数

        CloseableHttpAsyncClient httpAsyncClient = null;
        if (isSetTimeout) {
            //setConnectTimeout：设置连接超时时间，单位毫秒
            //setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection 超时时间，单位毫秒。
            //setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。
            RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();
            httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
        } else {
            httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(connectionManager).build();
        }
        httpAsyncClient.start();
        return httpAsyncClient;
    }

}
