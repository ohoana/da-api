package com.globits.da.service;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.exception.InvalidInputException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Page<EmployeeDto> getPage(int pageIndex, int pageSize);
    Page<EmployeeDto> search(EmployeeSearchDto searchDto) throws InvalidInputException;
    EmployeeDto findByCode(String code);
    List<EmployeeDto> getAll();
    List<EmployeeDto> addFromExcel(MultipartFile file) throws InvalidInputException, IOException;
    List<EmployeeDto> save(List<EmployeeDto> employeeDtoList) throws InvalidInputException;
    List<EmployeeDto> update(List<EmployeeDto> employeeDtoList) throws InvalidInputException;
    Boolean deleteById(UUID id);
    Workbook getExcel();
    Boolean isValidPage(int pageIndex, int pageSize);
}
