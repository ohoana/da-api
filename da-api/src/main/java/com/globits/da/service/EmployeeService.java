package com.globits.da.service;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Page<EmployeeDto> getPage(int pageIndex, int pageSize);
    Page<EmployeeDto> search(EmployeeSearchDto searchDto);
    EmployeeDto findByCode(String code);
    List<EmployeeDto> getAll();
    List<EmployeeDto> getFromExcel(MultipartFile file);
    List<EmployeeDto> save(List<EmployeeDto> employeeDtoList);
    List<EmployeeDto> update(List<EmployeeDto> employeeDtoList);
    Boolean deleteById(UUID id);
    Workbook getExcel();
    Boolean isValidEmployee(EmployeeDto employeeDto, Class<?> group, Integer rowIndex);
}
