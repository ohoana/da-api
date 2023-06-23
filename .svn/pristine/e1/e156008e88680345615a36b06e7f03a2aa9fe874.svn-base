package com.globits.da.validator.validator;

import com.globits.da.validator.constraint.NotContainSpace;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotContainSpaceValidator implements ConstraintValidator<NotContainSpace, String> {

    @Override
    public void initialize(NotContainSpace constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^\\S{6,10}$");
    }
}
