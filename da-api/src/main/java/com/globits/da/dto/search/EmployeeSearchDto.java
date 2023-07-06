package com.globits.da.dto.search;

public class EmployeeSearchDto {
    private final int pageIndex;
    private final int pageSize;
    private final String name;
    private final String email;
    private final String code;
    private final String phone;

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
