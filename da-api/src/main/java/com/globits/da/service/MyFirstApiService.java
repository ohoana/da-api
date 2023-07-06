package com.globits.da.service;

import com.globits.da.dto.MyFirstApiDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface MyFirstApiService {
    String getResource();
    MyFirstApiDto getFromJson(HttpServletRequest request);
    MyFirstApiDto getFromFormData(HttpServletRequest request);
    void readExcelFile(MultipartFile file) throws IOException;
}
