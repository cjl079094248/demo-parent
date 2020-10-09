package com.demo.service;

import com.demo.exception.CommException;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    /**
     * 文件输出
     * @param fileName
     * @param response
     */
    public void outputFile(String fileName, HttpServletResponse response);
    /**
     * 向输出流写出异常
     * @param servletResponse
     * @param msg
     * @param code
     */
    public void setVerifySignResponse(HttpServletResponse servletResponse, String msg, Integer code);
}
