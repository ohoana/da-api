package com.globits.da.dto.search;

public class EmployeeSearchDto {
    private int pageIndex;
    private int pageSize;
    private String name;
    private String email;
    private String code;
    private String phone;

    public EmployeeSearchDto() {
    }

    public EmployeeSearchDto(int pageIndex, int pageSize, String name, String email, String code, String phone) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.name = name;
        this.email = email;
        this.code = code;
        this.phone = phone;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public String getPhone() {
        return phone;
    }

}
