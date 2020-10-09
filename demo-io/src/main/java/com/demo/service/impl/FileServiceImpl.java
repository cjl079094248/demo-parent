package com.demo.service.impl;

import com.demo.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    @Value("${transcode.path}")
    private String transcodePath;

    @Override
    public void outputFile(String fileName, HttpServletResponse response) {
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
            }
        } catch (IOException e) {

        } finally {
            if(out!=null){
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getContentType(String fileFullPath){
        String type = null;
        Path path = Paths.get(fileFullPath);
        try {
            type = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return type;
    }


}
