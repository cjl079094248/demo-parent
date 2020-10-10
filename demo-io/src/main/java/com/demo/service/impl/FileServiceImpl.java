package com.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.demo.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {
    @Value("${transcode.path}")
    private String transcodePath;

    @Override
    public void outputFile(String fileName, HttpServletResponse response) {
        System.out.println("==================进入文件下载服务==================");
        File file = new File(transcodePath + fileName);
        FileInputStream in = null;
        OutputStream out = null;

        try {
            if (file.exists()) {
                response.setContentLengthLong(file.length());
                response.setContentType(getContentType(transcodePath + fileName));
                response.addHeader("Accept-Ranges", "bytes");
                response.addHeader("Content-Range", "bytes " + 0 + "-" + (file.length()-1) + "/" + file.length());
                response.setCharacterEncoding("utf-8");
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));


                in = new FileInputStream(file);
                out = response.getOutputStream();

                byte[] buf = new byte[1024];
                int bytesRead;

                while ((bytesRead = in.read(buf)) > 0) {
                    out.write(buf, 0, bytesRead);
                    out.flush();
                }
            }else{
                System.out.println("==================" + fileName + "不存在==================");
                setVerifySignResponse(response,fileName + "不存在",500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setVerifySignResponse(response,e.getMessage(),500);
        } finally {
            if(out!=null){
                try {
                    in.close();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void setVerifySignResponse(HttpServletResponse servletResponse, String msg, Integer code) {
        servletResponse.setContentType("application/json; charset=utf-8");
        servletResponse.setCharacterEncoding("UTF-8");

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("msg",msg);
        resultMap.put("code",code);
        resultMap.put("timestamp",System.currentTimeMillis());
        resultMap.put("data",null);

        OutputStream out = null;
        try {
            String json = JSON.toJSONString(resultMap);
            out = servletResponse.getOutputStream();
            out.write(json.getBytes("UTF-8"));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取文件MIME TYPE
     * @param fileFullPath
     * @return
     */
    private String getContentType(String fileFullPath){
        String type = null;
        Path path = Paths.get(fileFullPath);
        try {
            type = Files.probeContentType(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }





}
