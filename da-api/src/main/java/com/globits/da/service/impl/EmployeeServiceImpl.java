package com.globits.da.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.commons.ApiMessageError;
import com.globits.da.consts.Excel;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.exception.InvalidDtoException;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.WriteExcelFile;
import com.globits.da.utils.InjectParam;
import com.globits.da.validation.ValidationEmployee;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, UUID> implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ValidationEmployee validationEmployee;
    private final InjectParam injectParam;
    private final ObjectMapper objectMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ValidationEmployee validationEmployee,
                               InjectParam injectParam, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.validationEmployee = validationEmployee;
        this.injectParam = injectParam;
        this.objectMapper = objectMapper;
    }

    @Override
    public EmployeeDto findByCode(String code) {
        return employeeRepository.findByCode(code);
    }

    @Override
    public Page<EmployeeDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return employeeRepository.getPage(pageable);
    }

    @Override
    public Page<EmployeeDto> search(EmployeeSearchDto searchDto) throws InvalidInputException {
        if(ObjectUtils.isEmpty(searchDto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        isValidPage(searchDto.getPageIndex(), searchDto.getPageSize());
        int pageIndex = searchDto.getPageIndex();
        int pageSize = searchDto.getPageSize();

        String whereSql = "";
        String sql = "select new com.globits.da.dto.EmployeeDto(entity) from Employee as entity where (1=1) ";
        String sqlCount = "select count(entity.id) from Employee as entity where (1=1) ";
        String orderBySql = "order by entity.id desc";

        whereSql = injectParam.updateQuery(searchDto, whereSql);
        sql += whereSql + orderBySql;
        sqlCount += whereSql;
        Query sqlQuery = manager.createQuery(sql, EmployeeDto.class);
        Query sqlCountQuery = manager.createQuery(sqlCount);
        injectParam.setParamQuery(searchDto, sqlQuery, sqlCountQuery);
        int offset = pageIndex * pageSize;
        sqlQuery.setFirstResult(offset);
        sqlQuery.setMaxResults(pageSize);

        @SuppressWarnings("unchecked")
        List<EmployeeDto> employeeDtoList = sqlQuery.getResultList();
        long count = (long) sqlCountQuery.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(employeeDtoList, pageable, count);
    }

    @Override
    public List<EmployeeDto> getAll() {
        return employeeRepository.getAll();
    }

    @Override
    public List<EmployeeDto> addFromExcel(MultipartFile file) throws InvalidInputException, IOException {
        List<EmployeeDto> result = new ArrayList<>();
        boolean hasError = false;
        XSSFWorkbook workbook;
        workbook = new XSSFWorkbook(file.getInputStream());
        int sheetIdx = 0;
        XSSFSheet sheet = workbook.getSheetAt(sheetIdx);
        XSSFSheet sheetError = workbook.cloneSheet(sheetIdx, "error");
        Iterator<Row> rowIterator = sheet.rowIterator();
        int rowIdx = 0;
        while(rowIterator.hasNext()) {
            XSSFRow row = (XSSFRow) rowIterator.next();
            String code = row.getCell(Excel.COLUMN_INDEX_CODE).getStringCellValue();
            String name = row.getCell(Excel.COLUMN_INDEX_NAME).getStringCellValue();
            String email = row.getCell(Excel.COLUMN_INDEX_EMAIL).getStringCellValue();
            String phone = row.getCell(Excel.COLUMN_INDEX_PHONE).getStringCellValue();
            Integer age = (int) row.getCell(Excel.COLUMN_INDEX_AGE).getNumericCellValue();
            UUID provinceId = UUID.fromString(row.getCell(Excel.COLUMN_INDEX_PROVINCE).getStringCellValue());
            UUID districtId = UUID.fromString(row.getCell(Excel.COLUMN_INDEX_DISTRICT).getStringCellValue());
            UUID townId = UUID.fromString(row.getCell(Excel.COLUMN_INDEX_TOWN).getStringCellValue());
            EmployeeDto employeeDto = new EmployeeDto(code, name, email, phone, age, provinceId, districtId, townId);
            try {
                validationEmployee.checkEmployeeValid(employeeDto, OnCreate.class);
            } catch (InvalidInputException ex) {
                XSSFRow rowError = sheetError.getRow(rowIdx);
                XSSFCell cellNote = rowError.createCell(Excel.COLUMN_INDEX_NOTE);
                String note = objectMapper.writeValueAsString(ex.getApiError());
                cellNote.setCellValue(note);
                hasError = true;
            }
            rowIdx++;
        }
        if(hasError) {
            OutputStream os = Files.newOutputStream(Paths.get(Excel.PATH_FILE_EXCEl_ERROR));
            workbook.write(os);
            Map<String, String> error = new HashMap<>();
            error.put("Excel", MessageConst.ERROR_EMPLOYEE_IN_EXCEL);
            throw new InvalidDtoException(error);
        }
        return save(result);
    }

    @Override
    @Transactional
    public List<EmployeeDto> save(List<EmployeeDto> employeeDtoList) throws InvalidInputException {
        if(ObjectUtils.isEmpty(employeeDtoList)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        List<Employee> employees = new ArrayList<>();
        for(EmployeeDto employeeDto : employeeDtoList) {
            validationEmployee.checkEmployeeValid(employeeDto, OnCreate.class);
            Employee employee = new Employee();
            injectParam.setEmployeeValue(employee, employeeDto, OnCreate.class);
            employees.add(employee);
        }
        List<Employee> employeeIterator = employeeRepository.saveAll(employees);
        return employeeIterator.stream().map(EmployeeDto::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeDto> update(List<EmployeeDto> employeeDtoList) throws InvalidInputException {
        if(ObjectUtils.isEmpty(employeeDtoList)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        List<Employee> employees = new ArrayList<>();
        for(EmployeeDto employeeDto : employeeDtoList) {
            Optional<Employee> employeeOpt = employeeRepository.findById(employeeDto.getId());
            if(employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();
                validationEmployee.checkEmployeeValid(employeeDto, OnUpdate.class);
                injectParam.setEmployeeValue(employee, employeeDto, OnUpdate.class);
                employees.add(employee);
            }
        }
        List<Employee> employeeIterator = employeeRepository.saveAll(employees);
        return employeeIterator.stream().map(EmployeeDto::new).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(UUID id) {
        if(!ObjectUtils.isEmpty(id) && employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Workbook getExcel() {
        return WriteExcelFile.writeToExcelFile(employeeRepository.getAll());
    }

    @Override
    public Boolean isValidPage(int pageIndex, int pageSize) {
        HashMap<String, String> error = new HashMap<>();
        if(pageIndex <= 0) {
            error.put("Page Index: ", MessageConst.PAGE_INDEX_ERROR);
        }
        if(pageSize < 1) {
            error.put("Page Size: ", MessageConst.PAGE_SIZE_ERROR);
        }
        if(!error.isEmpty()) {
            throw new InvalidDtoException(error);
        }
        return true;
    }
}
