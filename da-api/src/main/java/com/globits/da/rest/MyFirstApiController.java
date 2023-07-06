package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.MyFirstApiDto;
import com.globits.da.service.MyFirstApiService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/myapi")
public class MyFirstApiController {
    private final MyFirstApiService myFirstApiService;

    public MyFirstApiController(MyFirstApiService myFirstApiService) {
        this.myFirstApiService = myFirstApiService;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @GetMapping()
    public String getMyApi() {
        return myFirstApiService.getResource() ;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping()
    public MyFirstApiDto postMyApi(@RequestBody MyFirstApiDto myFirstApiDto) {
        return myFirstApiDto;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/form")
    public MyFirstApiDto postMyApiForm(@RequestParam int code,
                                       @RequestParam String name,
                                       @RequestParam Integer age) {
        return new MyFirstApiDto(code, name, age);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/path/{code}/{name}/{age}")
    public MyFirstApiDto postMyApiPath(@PathVariable int code,
                                       @PathVariable String name,
                                       @PathVariable Integer age) {
        return new MyFirstApiDto(code, name, age);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/anno")
    public MyFirstApiDto postMyApiAnno(HttpServletRequest request) {
        return myFirstApiService.getFromFormData(request);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/json")
    public MyFirstApiDto postMyApiJson(HttpServletRequest request) {
        return myFirstApiService.getFromJson(request);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN})
    @PostMapping("/excel")
    public void readExcelFile(@RequestParam("file") MultipartFile[] submissions) {
        try {
            for(MultipartFile multipartFile : submissions) {
                myFirstApiService.readExcelFile(multipartFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
