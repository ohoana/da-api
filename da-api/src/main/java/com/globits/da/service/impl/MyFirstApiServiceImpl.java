package com.globits.da.service.impl;

import com.globits.da.dto.MyFirstApiDto;
import com.globits.da.service.MyFirstApiService;
import com.globits.da.exception.InvalidDtoException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyFirstApiServiceImpl implements MyFirstApiService {
    @Override
    public String getResource() {
        return "MyFirstApiService";
    }

    @Override
    public MyFirstApiDto getFromJson(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder buffer = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(payload, MyFirstApiDto.class);
        } catch(IOException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("Json", "is not valid");
            throw new InvalidDtoException(errors);
        }
    }

    @Override
    public MyFirstApiDto getFromFormData(HttpServletRequest request) {
        Integer code = Integer.parseInt(request.getParameter("code"));
        String name = request.getParameter("name");
        Integer age = Integer.parseInt(request.getParameter("age"));
        return new MyFirstApiDto(code, name, age);
    }

    @Override
    public void readExcelFile(MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            System.out.println("empty file.");
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        int sheets = workbook.getNumberOfSheets();
        int sheetIndex = 0;
        Sheet sheet;
        while(sheetIndex < sheets) {
            sheet = workbook.getSheetAt(sheetIndex++);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    CellType cellType = cell.getCellTypeEnum();
                    Object value = null;
                    switch (cellType) {
                        case NUMERIC: {
                            value = cell.getNumericCellValue();
                            break;
                        }
                        case STRING: {
                            value = cell.getStringCellValue();
                            break;
                        }
                        case BOOLEAN: {
                            value = cell.getBooleanCellValue();
                            break;
                        }
                        case _NONE:
                        case FORMULA:
                        case ERROR:
                        case BLANK: {
                            value = "error type";
                            break;
                        }
                    }
                    System.out.println(value);
                }
            }
        }
    }
}
