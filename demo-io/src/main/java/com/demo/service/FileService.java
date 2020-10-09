package com.demo.service;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    public void outputFile(String fileName, HttpServletResponse response);
}
