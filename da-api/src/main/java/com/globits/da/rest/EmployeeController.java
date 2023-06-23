package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.exception.InvalidEmployeeDtoException;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.apache.http.HttpResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EmployeeDto>> getAllEmployee() {
        List<EmployeeDto> result = employeeService.getAllEmployee();
        return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/paging/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<EmployeeDto>> getEmployeeInPage(@PathVariable Integer pageIndex,
                                                               @PathVariable Integer pageSize) {
        Page<EmployeeDto> result = employeeService.getPage(pageIndex, pageSize);
        return new ResponseEntity<Page<EmployeeDto>>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Page<EmployeeDto>> getEmployeeBySearch(@RequestBody EmployeeSearchDto dto) {
        Page<EmployeeDto> result = employeeService.searchEmployee(dto);
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
    public ResponseEntity<EmployeeDto> addEmployee(@Validated(OnCreate.class) @RequestBody EmployeeDto employeeDto) {
        EmployeeDto result = employeeService.saveOrUpdate(employeeDto, null);
        return new ResponseEntity<EmployeeDto>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<EmployeeDto> updateEmployee(@Validated(OnUpdate.class) @RequestBody EmployeeDto employeeDto,
                                                      @PathVariable UUID id) {
        EmployeeDto result = employeeService.saveOrUpdate(employeeDto, id);
        return new ResponseEntity<EmployeeDto>(result, HttpStatus.OK);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable(name = "id") UUID id) {
        Boolean result = employeeService.delete(id);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEmployeeDtoException.class)
    public Map<String, String> handlerInvalidEmployeeDto(InvalidEmployeeDtoException e) {
        return e.getErrors();
    }
}