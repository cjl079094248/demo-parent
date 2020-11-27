package com.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("all")
public class RestTemplateUtil {
    public static HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

    static {
        httpRequestFactory.setConnectionRequestTimeout(150000);
        httpRequestFactory.setConnectTimeout(150000);
        httpRequestFactory.setReadTimeout(150000);
    }

    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        return restTemplate;
    }

    /**
     * 封装的get请求，暂时只支持map传参，并且value只支持基本类型和String
     *
     * @return
     */
    public static Object get(Map<String, Object> dataValue, String requestUrl) {

        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        Iterator<Map.Entry<String, Object>> iterator = dataValue.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        while (iterator.hasNext()) {
            Map.Entry<String, Object> e = iterator.next();
            String key = e.getKey();
            String value = e.getValue() == null ? "" : e.getValue().toString();
            sb.append(key + "=").append(value).append("&");
        }
        String url = sb.toString();
        url = url.substring(0, url.length() - 1);
        url = requestUrl + url;
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<JSONObject>(headers),
                Object.class);
        Object obj = null;

        // 为大数据分析提供日志
        long startTime = System.currentTimeMillis();

        try {
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return obj;

    }

    public static Object post(Map<String, Object> map, String url) {

        JSONObject param = new JSONObject();
        for (String key : map.keySet()) {
            param.put(key, map.get(key));
        }

        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<JSONObject> formEntity = new HttpEntity<JSONObject>(param, headers);

        Object obj = null;

        // 为大数据分析提供日志
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            //Integer httpStatus = responseEntity.getStatusCode().value();
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return obj;
    }

    public static Object postWithHeadParam(Map<String, Object> map, String url,HttpHeaders headers) {
        JSONObject param = new JSONObject();
        for (String key : map.keySet()) {
            param.put(key, map.get(key));
        }

        RestTemplate restTemplate = getRestTemplate();
        HttpEntity<JSONObject> formEntity = new HttpEntity<JSONObject>(param, headers);

        Object obj = null;

        // 为大数据分析提供日志
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            //Integer httpStatus = responseEntity.getStatusCode().value();
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }


    public static Object postFile(Map<String, Object> map, String url, String urlPath, MultipartFile file) throws Exception {
        String requestUrl = url + urlPath;

        JSONObject param = new JSONObject();
        for (String key : map.keySet()) {
            param.put(key, map.get(key));
        }

        RestTemplate restTemplate = getRestTemplate();

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        File toFile = multipartFileToFile(file);

        //设置请求体，注意是LinkedMultiValueMap
        FileSystemResource fileSystemResource = new FileSystemResource(toFile);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("content", fileSystemResource);
        //form.add("filename",fileName);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(form, headers);

        Object obj = null;

        // 为大数据分析提供日志
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(requestUrl, formEntity, Object.class);
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTempFile(toFile);
        }

        return obj;
    }


    public static Object postUrlencodedRequest(Map<String, Object> map, String url) {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        for (String key : map.keySet()) {
            param.add(key, map.get(key));
        }

        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<MultiValueMap<String, Object>>(param, headers);

        Object obj = null;

        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            //Integer httpStatus = responseEntity.getStatusCode().value();
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return obj;
    }

    public static Object post4Param(Map<String, Object> map, String url) {

        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSONString(map), headers);
        Object obj = null;

        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static Object postByMultiMap(Map<String, Object> map, String url) {
        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSONString(map), headers);
        Object obj = null;

        long startTime = System.currentTimeMillis();

        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return obj;
    }


    public static Object post1(Map<String, Object> map, String url) {
        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSONString(map), headers);

        Object obj = null;
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static Object post2(Map<String, Object> map, String url) {
        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSONString(map), headers);

        Object obj = null;
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return obj;
    }


    public static Object post3(Map<String, Object> map, String url) {
        RestTemplate restTemplate = getRestTemplate();
        // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSONString(map), headers);

        Object obj = null;
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, formEntity, Object.class);
            obj = responseEntity.getBody();
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return obj;
    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

}
