package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
public class RestEmployeeController {
    private final EmployeeService employeeService;

    public RestEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EmployeeDto>> getAll() {
        List<EmployeeDto> result = employeeService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/paging/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<EmployeeDto>> getPage(@PathVariable Integer pageIndex,
                                                               @PathVariable Integer pageSize) {
        Page<EmployeeDto> result = employeeService.getPage(pageIndex, pageSize);
        return ResponseEntity.ok()
                .body(result);
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
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=data.xlsx");
            ServletOutputStream outputStream1 = response.getOutputStream();
            workbook.write(outputStream1);
            workbook.close();
            outputStream1.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<EmployeeDto>> add(@Validated(OnCreate.class) @RequestBody EmployeeDto employeeDto) {
        List<EmployeeDto> result = null;
        if(employeeService.isValidEmployee(employeeDto, OnCreate.class, null)) {
            result = employeeService.save(Collections.singletonList(employeeDto));
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public ResponseEntity<?> addByExcelFile(@RequestParam("file")MultipartFile file) {
        List<EmployeeDto> result = new ArrayList<>();
        if(file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(result);
        }
        List<EmployeeDto> dtoList = employeeService.getFromExcel(file);
        if(ObjectUtils.isEmpty(dtoList)) {
            return ResponseEntity.ok().body("Error in Excel");
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<List<EmployeeDto>> update(@Validated(OnUpdate.class) @RequestBody EmployeeDto employeeDto) {
        List<EmployeeDto> result = null;
        if(employeeService.isValidEmployee(employeeDto, OnUpdate.class, null)) {
            result = employeeService.update(Collections.singletonList(employeeDto));
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "id") UUID id) {
        Boolean result = employeeService.deleteById(id);
        return ResponseEntity.ok()
                .body(result);
    }
}
