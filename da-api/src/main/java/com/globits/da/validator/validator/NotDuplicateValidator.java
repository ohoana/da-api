package com.globits.da.validator.validator;

import com.globits.da.service.EmployeeService;
import com.globits.da.validator.constraint.NotDuplicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotDuplicateValidator implements ConstraintValidator<NotDuplicate, String> {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ObjectUtils.isEmpty(employeeService.findByCode(value));
    }
}
