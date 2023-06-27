package com.globits.da.service;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Page<EmployeeDto> getPage(int pageIndex, int pageSize);
    Page<EmployeeDto> search(EmployeeSearchDto searchDto);

    EmployeeDto findByCode(String code);
    List<EmployeeDto> getAll();
    EmployeeDto saveOrUpdate(EmployeeDto employeeDto, UUID id);

    List<EmployeeDto> saveList(List<EmployeeDto> employeeDtos);
    Boolean deleteById(UUID id);
    Workbook getExcel();

    Boolean isValidEmployee(EmployeeDto employeeDto, Class group);
}
