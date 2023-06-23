package com.globits.da.service;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.utils.exception.InvalidEmployeeDtoException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Page<EmployeeDto> getPage(int pageIndex, int pageSize);
    Page<EmployeeDto> searchEmployee(EmployeeSearchDto searchDto);

    EmployeeDto findByCode(String code);
    List<EmployeeDto> getAllEmployee();
    EmployeeDto saveOrUpdate(EmployeeDto employeeDto, UUID id);
    Boolean delete(UUID id);
    Workbook getExcel();

    Boolean isValidEmployee(EmployeeDto employeeDto);
}
