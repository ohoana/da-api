package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.MyFirstApiDto;
import com.globits.da.service.MyFirstApiService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

@RestController
@RequestMapping("/api/myapi")
public class MyFirstApiController {

    @Autowired
    private MyFirstApiService myFirstApiService;

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @GetMapping()
    public String getMyApi() {
        return myFirstApiService.getResource() ;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping()
    public MyFirstApiDto postMyApi(@RequestBody MyFirstApiDto myFirstApiDto) {
        return myFirstApiDto;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/form")
    public MyFirstApiDto postMyApiForm(@RequestParam int code,
                                       @RequestParam String name,
                                       @RequestParam Integer age) {
        MyFirstApiDto dto = new MyFirstApiDto(code, name, age);
        return dto;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/path/{code}/{name}/{age}")
    public MyFirstApiDto postMyApiPath(@PathVariable int code,
                                       @PathVariable String name,
                                       @PathVariable Integer age) {
        MyFirstApiDto dto = new MyFirstApiDto(code, name, age);
        return dto;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/anno")
    public MyFirstApiDto postMyApiAnno(HttpServletRequest request) {
        Integer code = Integer.parseInt(request.getParameter("code"));
        String name = request.getParameter("name");
        Integer age = Integer.parseInt(request.getParameter("age"));
        MyFirstApiDto dto = new MyFirstApiDto(code, name, age);
        return dto;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/json")
    public MyFirstApiDto postMyApiJson(HttpServletRequest request) {
        MyFirstApiDto dto = new MyFirstApiDto();
        try {
            BufferedReader reader = request.getReader();
            StringBuffer buffer = new StringBuffer();

            String line = "";
            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();

            ObjectMapper objectMapper = new ObjectMapper();

            dto = objectMapper.readValue(payload, MyFirstApiDto.class);
            return dto;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/excel")
    public void postExcelFile(@RequestParam("file") MultipartFile[] submissions) {
        try {
            for(MultipartFile multipartFile : submissions) {
                if(multipartFile.isEmpty()) {
                    System.out.println("empty file.");
                    return;
                }
                XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
                int sheets = workbook.getNumberOfSheets();
                int sheetIndex = 0;
                Sheet sheet;
                while(sheetIndex < sheets) {
                    sheet = workbook.getSheetAt(sheetIndex++);
                    Iterator<Row> iteratorRow = sheet.iterator();
                    while(iteratorRow.hasNext()) {
                        Row row = iteratorRow.next();

                        Iterator<Cell> iteratorCell = row.iterator();
                        while(iteratorCell.hasNext()) {
                            Cell cell = iteratorCell.next();
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
                                case BLANK:{
                                    value = "error type";
                                    break;
                                }
                            }
                            System.out.println(value);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
