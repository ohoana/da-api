package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.domain.Employee;
import com.globits.da.validator.constraint.NotContainSpace;
import com.globits.da.validator.constraint.NotDuplicate;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.*;
import java.util.UUID;

public class EmployeeDto extends BaseObject {
    @NotContainSpace(groups = {OnUpdate.class, OnCreate.class})
    @NotDuplicate(groups = {OnCreate.class})
    private String code;
    @NotBlank(message = "Name must not be empty",
            groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @Pattern(regexp = "^[\\w|.]+@[\\w]+\\.[\\w]{2,3}$", message = "Email is not valid",
            groups = {OnUpdate.class, OnCreate.class})
    private String email;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})", message = "Phone-number is not valid",
            groups = {OnUpdate.class, OnCreate.class})
    private String phone;
    @Min(value = 0,message = "Age must not be negative",
            groups = {OnUpdate.class, OnCreate.class})
    private Integer age;

    @NotNull(message = "Province must not be empty",
            groups = {OnCreate.class})
    private UUID provinceId;

    @NotNull(message = "District must not be empty",
            groups = {OnCreate.class})
    private UUID districtId;

    @NotNull(message = "Town must not be empty",
            groups = {OnCreate.class})
    private UUID townId;
    public EmployeeDto() {
    }

    public EmployeeDto(Employee employee) {
        if(!ObjectUtils.isEmpty(employee)) {
            this.setId(employee.getId());
            this.code = employee.getCode();
            this.name = employee.getName();
            this.email = employee.getEmail();
            this.phone = employee.getPhone();
            this.age = employee.getAge();
            this.provinceId = employee.getProvince().getId();
            this.districtId = employee.getDistrict().getId();
            this.townId = employee.getTown().getId();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UUID getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(UUID provinceId) {
        this.provinceId = provinceId;
    }

    public UUID getDistrictId() {
        return districtId;
    }

    public void setDistrictId(UUID districtId) {
        this.districtId = districtId;
    }

    public UUID getTownId() {
        return townId;
    }

    public void setTownId(UUID townId) {
        this.townId = townId;
    }
}
