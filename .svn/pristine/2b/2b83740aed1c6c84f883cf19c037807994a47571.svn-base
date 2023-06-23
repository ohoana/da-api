package com.globits.da.utils;

import com.globits.da.dto.EmployeeDto;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class WriteExcelFile {
    private static final int COLUMN_INDEX_ID = 0;
    private static final int COLUMN_INDEX_CODE = 1;
    private static final int COLUMN_INDEX_NAME = 2;
    private static final int COLUMN_INDEX_EMAIL = 3;
    private static final int COLUMN_INDEX_PHONE = 4;
    private static final int COLUMN_INDEX_AGE = 5;

    public static Workbook writeToExcelFile(List<EmployeeDto> employeeDtoList) {
        Workbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("employee");

        int rowIdx = 0;
        writeHeader(sheet, rowIdx++);

        for(EmployeeDto employeeDto : employeeDtoList) {
            XSSFRow row = sheet.createRow(rowIdx++);
            writeEmployee(employeeDto, row);
        }

//        try {
//            OutputStream outputStream = new FileOutputStream("C:\\Users\\hoant\\Desktop\\data.xlsx");
//            workbook.write(outputStream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return workbook;
    }

    public static void writeEmployee(EmployeeDto employeeDto, XSSFRow row) {
        XSSFCell cell = null;
        cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellValue(employeeDto.getId());

        cell = row.createCell(COLUMN_INDEX_CODE);
        cell.setCellValue(employeeDto.getCode());

        cell = row.createCell(COLUMN_INDEX_NAME);
        cell.setCellValue(employeeDto.getName());

        cell = row.createCell(COLUMN_INDEX_EMAIL);
        cell.setCellValue(employeeDto.getEmail());

        cell = row.createCell(COLUMN_INDEX_PHONE);
        cell.setCellValue(employeeDto.getPhone());

        cell = row.createCell(COLUMN_INDEX_AGE);
        cell.setCellValue(employeeDto.getAge());
    }

    private static void writeHeader(Sheet sheet, int rowIdx) {
        XSSFRow row = (XSSFRow) sheet.createRow(rowIdx);

        XSSFCell cell = null;
        cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellValue("ID");

        cell = row.createCell(COLUMN_INDEX_CODE);
        cell.setCellValue("CODE");

        cell = row.createCell(COLUMN_INDEX_NAME);
        cell.setCellValue("NAME");

        cell = row.createCell(COLUMN_INDEX_EMAIL);
        cell.setCellValue("EMAIL");

        cell = row.createCell(COLUMN_INDEX_PHONE);
        cell.setCellValue("PHONE");

        cell = row.createCell(COLUMN_INDEX_AGE);
        cell.setCellValue("AGE");
    }
}
