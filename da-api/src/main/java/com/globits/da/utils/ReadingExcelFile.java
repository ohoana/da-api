package com.globits.da.utils;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReadingExcelFile {
    @Autowired
    private EmployeeService employeeService;
    private static final int COLUMN_INDEX_CODE = 0;
    private static final int COLUMN_INDEX_NAME = 1;
    private static final int COLUMN_INDEX_EMAIL = 2;
    private static final int COLUMN_INDEX_PHONE = 3;
    private static final int COLUMN_INDEX_AGE = 4;
    private static final int COLUMN_INDEX_PROVINCE = 5;
    private static final int COLUMN_INDEX_DISTRICT = 6;
    private static final int COLUMN_INDEX_TOWN = 7;

    public List<EmployeeDto> getEmployee(XSSFWorkbook workbook) {
        List<EmployeeDto> result = new ArrayList<>();
        Set<String> codeSet = new HashSet<>();
        int numOfSheet = workbook.getNumberOfSheets();
        Sheet sheet;
        int sheetIdx = 0;
        while(sheetIdx < numOfSheet) {
            sheet = workbook.getSheetAt(sheetIdx++);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowIdx = 0;
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String code = row.getCell(COLUMN_INDEX_CODE).getStringCellValue();
                    String name = row.getCell(COLUMN_INDEX_NAME).getStringCellValue();
                    String email = row.getCell(COLUMN_INDEX_EMAIL).getStringCellValue();
                    String phone = row.getCell(COLUMN_INDEX_PHONE).getStringCellValue();
                    Integer age = (int) row.getCell(COLUMN_INDEX_AGE).getNumericCellValue();
                    UUID provinceId = UUID.fromString(row.getCell(COLUMN_INDEX_PROVINCE).getStringCellValue());
                    UUID districtId = UUID.fromString(row.getCell(COLUMN_INDEX_DISTRICT).getStringCellValue());
                    UUID townId = UUID.fromString(row.getCell(COLUMN_INDEX_TOWN).getStringCellValue());
                    EmployeeDto employeeDto = new EmployeeDto();
                    employeeDto.setCode(code);
                    employeeDto.setName(name);
                    employeeDto.setEmail(email);
                    employeeDto.setPhone(phone);
                    employeeDto.setAge(age);
                    employeeDto.setProvinceId(provinceId);
                    employeeDto.setDistrictId(districtId);
                    employeeDto.setTownId(townId);
                    if(codeSet.contains(code)) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("Excel", "at line " + rowIdx + ", Code must not be duplicated");
                        throw new InvalidDtoException(errors);
                    }
                    codeSet.add(code);
                    result.add(employeeDto);
                } catch (Exception e) {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("Excel", "at line " + rowIdx);
                    throw new InvalidDtoException(errors);
                }
                rowIdx++;
            }
        }
        return result;
    }
}
