package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.consts.MessageConst;
import com.globits.da.consts.RegexConst;
import com.globits.da.domain.Employee;
import com.globits.da.validator.constraint.NotContainSpace;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

public class EmployeeDto extends BaseObject {
    @NotContainSpace(groups = {OnCreate.class, OnUpdate.class})
    private String code;
    @NotBlank(message = MessageConst.NOT_EMPTY,
            groups = {OnCreate.class, OnUpdate.class})
    private String name;
    @Pattern(regexp = RegexConst.EMAIL_REGEX, message = MessageConst.NOT_VALID,
            groups = {OnCreate.class, OnUpdate.class})
    private String email;
    @Pattern(regexp = RegexConst.PHONE_REGEX, message = MessageConst.NOT_VALID,
            groups = {OnCreate.class, OnUpdate.class})
    private String phone;
    @Min(value = 1,message = MessageConst.NOT_VALID,
            groups = {OnCreate.class, OnUpdate.class})
    private Integer age;
    @NotNull(message = MessageConst.NOT_EMPTY,
            groups = {OnCreate.class})
    private UUID provinceId;
    @NotNull(message = MessageConst.NOT_EMPTY,
            groups = {OnCreate.class})
    private UUID districtId;
    @NotNull(message = MessageConst.NOT_EMPTY,
            groups = {OnCreate.class})
    private UUID townId;

    public EmployeeDto() {
    }

    public EmployeeDto(String code, String name, String email, String phone,
                       Integer age, UUID provinceId, UUID districtId, UUID townId) {
        this.code = code;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.townId = townId;
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
