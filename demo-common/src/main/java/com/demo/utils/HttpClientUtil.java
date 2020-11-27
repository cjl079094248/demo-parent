package com.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("all")
public class HttpClientUtil {

    private static CloseableHttpClient httpclient;

    private final static int CONNECTION_REQUEST_TIMEOUT = 15000;
    private final static int SOCKET_TIMEOUT = 15000;
    private final static int CONNECT_TIMEOUT = 15000;

    private static CloseableHttpClient getHttpClient() {
        if(httpclient == null) {
            PoolingHttpClientConnectionManager connectManager = new PoolingHttpClientConnectionManager();
            ConnectionConfig connectionConfig =  ConnectionConfig.DEFAULT;
            connectManager.setDefaultConnectionConfig(connectionConfig);
            connectManager.setMaxTotal(500);
            connectManager.setDefaultMaxPerRoute(500);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .build();

            httpclient = HttpClients.custom()
                    .setConnectionManager(connectManager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
        return httpclient;
    }

    /**
     * get请求，返回的数据格式位json或者json数组
     * @param map
     * @param url
     * @param token
     * @return
     */
    public static Object getForJsonStr(Map<String,Object> map, String url) {
        String origUrl = url;
        Object json = null;
        if(null != map && map.size() > 0){
            StringBuffer stringBuffer = new StringBuffer(url);
            Iterator iterator = map.entrySet().iterator();
            if (iterator.hasNext()) {
                stringBuffer.append("?");
                Object element;
                while (iterator.hasNext()) {
                    element = iterator.next();
                    Map.Entry<String, Object> entry = (Map.Entry) element;
                    //过滤value为null，value为null时进行拼接字符串会变成 "null"字符串
                    if (entry.getValue() != null) {
                        stringBuffer.append(element).append("&");
                    }
                    url = stringBuffer.substring(0, stringBuffer.length() - 1);
                }
            }
        }

        CloseableHttpClient httpclient = getHttpClient();
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        /*RequestConfig requestConfig = RequestConfig.custom()
        .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
        .setSocketTimeout(SOCKET_TIMEOUT)
        .setConnectTimeout(CONNECT_TIMEOUT)
        .build();
        httpGet.setConfig(requestConfig);*/

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();

        try {
            response = httpclient.execute(httpGet);
            long endTime = System.currentTimeMillis();

            //Integer httpStatus = response.getStatusLine().getStatusCode();
            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }

        } catch(Exception e){
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        } finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }
    public static Object getForJsonStrObject(Map<String,Object> map, String url) {
        Object json = null;
        if(null != map && map.size() > 0){
            StringBuffer stringBuffer = new StringBuffer(url);
            Iterator iterator = map.entrySet().iterator();
            if (iterator.hasNext()) {
                stringBuffer.append("?");
                Object element;
                while (iterator.hasNext()) {
                    element = iterator.next();
                    Map.Entry<String, Object> entry = (Map.Entry) element;
                    //过滤value为null，value为null时进行拼接字符串会变成 "null"字符串
                    if (entry.getValue() != null) {
                        stringBuffer.append(element).append("&");
                    }
                    url = stringBuffer.substring(0, stringBuffer.length() - 1);
                }
            }
        }

        CloseableHttpClient httpclient = getHttpClient();
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        /*RequestConfig requestConfig = RequestConfig.custom()
        .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
        .setSocketTimeout(SOCKET_TIMEOUT)
        .setConnectTimeout(CONNECT_TIMEOUT)
        .build();
        httpGet.setConfig(requestConfig);*/

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();

        try {
            response = httpclient.execute(httpGet);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }

        } catch(Exception e){
            long endTime = System.currentTimeMillis();
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        } finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseContent;
    }
    /**
     * contentType为application/x-www-form-urlencoded的请求
     * @param map
     * @param url
     * @param token
     * @return
     */
    public static Object getForUrlEncoded(Map<String,Object> map, String url) {
        Object json = null;
        if(null != map && map.size() > 0){
            StringBuffer stringBuffer = new StringBuffer(url);
            Iterator iterator = map.entrySet().iterator();
            if (iterator.hasNext()) {
                stringBuffer.append("?");
                Object element;
                while (iterator.hasNext()) {
                    element = iterator.next();
                    Map.Entry<String, Object> entry = (Map.Entry) element;
                    //过滤value为null，value为null时进行拼接字符串会变成 "null"字符串
                    if (entry.getValue() != null) {
                        stringBuffer.append(element).append("&");
                    }
                    url = stringBuffer.substring(0, stringBuffer.length() - 1);
                }
            }
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();

        try {
            response = httpclient.execute(httpGet);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }

        } catch(Exception e){
            long endTime = System.currentTimeMillis();
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        } finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }

    /**
     * 头部加key
     * @param map
     * @param url
     * @return
     */
    public static Object getByHeaderKey(Map<String,Object> map, String url) {
        Object json = null;
        if(null != map && map.size() > 0){
            StringBuffer stringBuffer = new StringBuffer(url);
            Iterator iterator = map.entrySet().iterator();
            if (iterator.hasNext()) {
                stringBuffer.append("?");
                Object element;
                while (iterator.hasNext()) {
                    element = iterator.next();
                    Map.Entry<String, Object> entry = (Map.Entry) element;
                    //过滤value为null，value为null时进行拼接字符串会变成 "null"字符串
                    if (entry.getValue() != null) {
                        stringBuffer.append(element).append("&");
                    }
                    url = stringBuffer.substring(0, stringBuffer.length() - 1);
                }
            }
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();

        try {
            response = httpclient.execute(httpGet);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        }finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        return json;
    }

    /**
     * post请求，请求参数位json，返回值也是json的
     * @param map
     * @param url
     * @return
     */
    public static Object postForJsonStr(Map<String,Object> map, String url){
        Object json = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        if(null != map && map.size() > 0){
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(map),"utf-8");
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();
        try {
            response = httpclient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        }finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }



    public static Object shjfcxPostForJsonStr(Map<String,Object> map, String url){
        Object json = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(120000).setSocketTimeout(120000).setConnectTimeout(120000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        if(null != map && map.size() > 0){
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(map),"utf-8");
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();
        try {
            response = httpclient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        }finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }


    /**
     * post请求，请求参数位application/xml，返回值也是json的
     * @param map
     * @param url
     * @return
     */
    public static Object xmlPostForJsonStr(Map<String,Object> map, String url){
        Object json = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/xml");
        if(null != map && map.size() > 0){
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(map),"utf-8");
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();
        try {
            response = httpclient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        }finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }


    public static Object postForJsonStr1(Map<String,Object> map, String url){
        Object json = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        if(null != map && map.size() > 0){
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(map),"utf-8");
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();
        try {
            response = httpclient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        }finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }


    public static Object postForJsonStr2(Map<String,Object> map, String url){
        Object json = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("tenant", "suZhouDao");
        httpPost.setHeader("userName", "suZhouDao");
        httpPost.setHeader("support", "zz");
        if(null != map && map.size() > 0){
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(map),"utf-8");
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();
        try {
            response = httpclient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        }finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }


    public static Object postForStr(String param, String url,String token){
        Object json = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader(new BasicHeader("Authorization", "Bearer " + token));
        if(!StringUtils.isEmpty(param)){
            StringEntity stringEntity = new StringEntity(param,"utf-8");
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        String responseContent = null;

        long startTime = System.currentTimeMillis();
        try {
            response = httpclient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            if(null != response){
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            if(!StringUtils.isEmpty(responseContent)){
                json = (Object) JSON.parse(responseContent);

                if(null!= json && json instanceof String){
                    json = (JSON) JSON.parse(json.toString());
                }
            }

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            e.printStackTrace();
            Map<String,Object> exceptionMap = new HashMap<String,Object>();
            exceptionMap.put("code",500);
            exceptionMap.put("msg","接口调用失败");
            json = exceptionMap;
        }finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }

}
