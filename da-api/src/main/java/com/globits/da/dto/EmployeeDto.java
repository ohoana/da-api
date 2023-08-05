package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.consts.MessageConst;
import com.globits.da.consts.RegexConst;
import com.globits.da.domain.Employee;
import com.globits.da.validator.constraint.NotContainSpace;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}
