package com.demo.controller;

import com.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    /**
     * 文件传输
     * @param fileName
     */
    @RequestMapping(value = "/outputFile", method = {RequestMethod.POST,RequestMethod.GET})
    public void outputFile(@RequestParam String fileName, HttpServletResponse response){
        fileService.outputFile(fileName,response);
    }

}