package com.globits.da.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeSearchDto {
    private int pageIndex;
    private int pageSize;
    private String name;
    private String email;
    private String code;
    private String phone;
}
