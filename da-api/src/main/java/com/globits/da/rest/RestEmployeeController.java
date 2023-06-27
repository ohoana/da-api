package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.ReadingExcelFile;
import com.globits.da.utils.exception.InvalidDtoException;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.apache.http.HttpResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/employee")
public class RestEmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReadingExcelFile readingExcelFile;

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EmployeeDto>> getAll() {
        List<EmployeeDto> result = employeeService.getAll();
        return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/paging/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<EmployeeDto>> getPage(@PathVariable Integer pageIndex,
                                                               @PathVariable Integer pageSize) {
        Page<EmployeeDto> result = employeeService.getPage(pageIndex, pageSize);
        return new ResponseEntity<Page<EmployeeDto>>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Page<EmployeeDto>> getBySearchDto(@RequestBody EmployeeSearchDto dto) {
        Page<EmployeeDto> result = employeeService.search(dto);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        Workbook workbook = employeeService.getExcel();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
//            workbook.write(outputStream);
//            workbook.close();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=data.xlsx");
            ServletOutputStream outputStream1 = response.getOutputStream();
            workbook.write(outputStream1);
            workbook.close();
            outputStream1.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=data.xlsx")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(outputStream.toByteArray());
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<EmployeeDto> add(@Validated(OnCreate.class) @RequestBody EmployeeDto employeeDto) {
        EmployeeDto result = null;
        if(employeeService.isValidEmployee(employeeDto, OnCreate.class)) {
            result = employeeService.saveOrUpdate(employeeDto, null);
        }
        return new ResponseEntity<EmployeeDto>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public ResponseEntity<List<EmployeeDto>> addByExcelFile(@RequestParam("file")MultipartFile file) {
        List<EmployeeDto> result = new ArrayList<>();
        if(file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(result);
        }
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            List<EmployeeDto> dtos = readingExcelFile.getEmployee(workbook);
            result = employeeService.saveList(dtos);
        } catch (IOException e) {
            System.out.println("error while reading file from addEmployeeExcelFile");
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<EmployeeDto> update(@Validated(OnUpdate.class) @RequestBody EmployeeDto employeeDto,
                                                      @PathVariable UUID id) {
        EmployeeDto result = null;
        if(employeeService.isValidEmployee(employeeDto, OnUpdate.class)) {
            result = employeeService.saveOrUpdate(employeeDto, id);
        }
        return new ResponseEntity<EmployeeDto>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "id") UUID id) {
        Boolean result = employeeService.deleteById(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlerInvalidArgument(MethodArgumentNotValidException e) {
        Map<String, String> mapError = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            mapError.put(error.getField(), error.getDefaultMessage());
        });
        return mapError;
    }
}
